package com.groupdocs.ui.annotation.resources;

import com.groupdocs.annotation.common.license.License;
import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.DocumentType;
import com.groupdocs.annotation.domain.FileDescription;
import com.groupdocs.annotation.domain.RowData;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.annotation.domain.containers.FileTreeContainer;
import com.groupdocs.annotation.domain.options.FileTreeOptions;
import com.groupdocs.annotation.domain.options.ImageOptions;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.ui.annotation.annotator.*;
import com.groupdocs.ui.annotation.entity.web.AnnotatedDocumentEntity;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.annotation.entity.web.TextRowEntity;
import com.groupdocs.ui.annotation.util.directory.DirectoryUtils;
import com.groupdocs.ui.annotation.views.Annotation;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.MediaType;
import com.groupdocs.ui.common.entity.web.UploadedDocumentEntity;
import com.groupdocs.ui.common.entity.web.LoadedPageEntity;
import com.groupdocs.ui.common.entity.web.DocumentDescriptionEntity;
import com.groupdocs.ui.common.resources.Resources;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * AnnotationResources
 *
 * @author Aspose Pty Ltd
 */

@Path(value = "/annotation")
public class AnnotationResources extends Resources {
    private final AnnotationImageHandler annotationImageHandler;
    private DirectoryUtils directoryUtils;
    private String[] supportedImageFormats = {"bmp", "jpeg", "jpg", "tiff", "tif", "png", "gif", "emf", "wmf", "dwg", "dicom", "djvu"};

    /**
     * Constructor
     * @param globalConfiguration global configuration object
     * @throws UnknownHostException
     */
    public AnnotationResources(GlobalConfiguration globalConfiguration) throws UnknownHostException {
        super(globalConfiguration);

        directoryUtils = new DirectoryUtils(globalConfiguration.getAnnotation());

        // create annotation application configuration
        AnnotationConfig config = new AnnotationConfig();
        config.setStoragePath(directoryUtils.getFilesDirectory().getPath());
        globalConfiguration.getAnnotation().setOutputDirectory(directoryUtils.getOutputDirectory().getPath());
        config.getFontDirectories().add(globalConfiguration.getAnnotation().getFontsDirectory());
        // set GroupDocs license
        License license = new License();
        license.setLicense(globalConfiguration.getApplication().getLicensePath());

        // initialize total instance for the Image mode
        annotationImageHandler = new AnnotationImageHandler(config);
    }

    /**
     * Get and set annotation page
     * @return html view
     */
    @GET
    public Annotation getView(){
        // initiate index page
        return new Annotation(globalConfiguration, DEFAULT_CHARSET);
    }

    /**
     * Get files and directories
     * @param request
     * @param response
     * @return files and directories list
     */
    @POST
    @Path(value = "/loadFileTree")
    public Object loadFileTree(@Context HttpServletRequest request, @Context HttpServletResponse response){
        // set response content type
        setResponseContentType(response, MediaType.APPLICATION_JSON);
        // get request body
        String requestBody = getRequestBody(request);
        String relDirPath = getJsonString(requestBody, "path");
        // get file list from storage path
        FileTreeOptions fileListOptions = new FileTreeOptions(relDirPath);
        // get temp directory name
        String tempDirectoryName =  new com.groupdocs.annotation.domain.config.AnnotationConfig().getTempFolderName();
        try{
            FileTreeContainer fileListContainer = annotationImageHandler.loadFileTree(fileListOptions);

            ArrayList<FileDescriptionEntity> fileList = new ArrayList<>();
            // parse files/folders list
            for(FileDescription fd : fileListContainer.getFileTree()){
                FileDescriptionEntity fileDescription = new FileDescriptionEntity();
                fileDescription.setGuid(fd.getGuid());
                // check if current file/folder is temp directory or is hidden
                if(tempDirectoryName.toLowerCase().equals(fd.getName()) || new File(fileDescription.getGuid()).isHidden()) {
                    // ignore current file and skip to next one
                    continue;
                } else {
                    // set file/folder name
                    fileDescription.setName(fd.getName());
                }
                // set file type
                fileDescription.setDocType(fd.getDocumentType());
                // set is directory true/false
                fileDescription.setDirectory(fd.isDirectory());
                // set file size
                fileDescription.setSize(fd.getSize());
                // add object to array list
                fileList.add(fileDescription);
            }
            return objectToJson(fileList);
        }catch (Exception ex){
            return generateException(response, ex);
        }
    }

    /**
     * Get document description
     * @param request
     * @param response
     * @return document description
     */
    @POST
    @Path(value = "/loadDocumentDescription")
    public Object loadDocumentDescription(@Context HttpServletRequest request, @Context HttpServletResponse response){
        // set response content type
        setResponseContentType(response, MediaType.APPLICATION_JSON);
        String password = "";
        try {
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            password = getJsonString(requestBody, "password");
            DocumentInfoContainer documentDescription;
            // get document info container
            documentDescription = annotationImageHandler.getDocumentInfo(new File(documentGuid).getName(), password);
            ArrayList<DocumentDescriptionEntity> pagesDescription = new ArrayList<>();
            // get info about each document page
            for(int i = 0; i < documentDescription.getPages().size(); i++) {
                //initiate custom Document description object
                DocumentDescriptionEntity description = new DocumentDescriptionEntity();
                // set current page info for result
                description.setHeight(documentDescription.getPages().get(i).getHeight());
                description.setWidth(documentDescription.getPages().get(i).getWidth());
                description.setNumber(documentDescription.getPages().get(i).getNumber());
                pagesDescription.add(description);
            }
            // return document description
            return objectToJson(pagesDescription);
        }catch (Exception ex){
            return generateException(response, ex);
        }
    }

    /**
     * Get document page
     * @param request
     * @param response
     * @return document page
     */
    @POST
    @Path(value = "/loadDocumentPage")
    public Object loadDocumentPage(@Context HttpServletRequest request, @Context HttpServletResponse response){
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            int pageNumber = getJsonInteger(requestBody, "page");
            String password = getJsonString(requestBody, "password");
            LoadedPageEntity loadedPage = new LoadedPageEntity();
            // set options
            ImageOptions imageOptions = new ImageOptions();
            imageOptions.setPageNumber(pageNumber);
            imageOptions.setCountPagesToConvert(1);
            // set password for protected document
            if(!password.isEmpty()) {
                imageOptions.setPassword(password);
            }
            // get page image
            byte[] bytes = IOUtils.toByteArray(annotationImageHandler.getPages(documentGuid, imageOptions).get(0).getStream());
            // encode ByteArray into String
            String incodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(incodedImage);
            // return loaded page object
            return objectToJson(loadedPage);
        }catch (Exception ex){
            return generateException(response, ex);
        }
    }

    /**
     * Download document
     * @param request
     * @param response
     * @return document
     */
    @GET
    @Path(value = "/downloadDocument")
    public Object downloadDocument(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        int count;
        byte[] buff = new byte[16 * 1024];
        OutputStream out = response.getOutputStream();
        // set response content type
        setResponseContentType(response, MediaType.APPLICATION_OCTET_STREAM);
        // get document path
        String documentGuid = request.getParameter("path");
        String fileName = new File(documentGuid).getName();
        // set response content disposition
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        BufferedOutputStream outStream = null;
        BufferedInputStream inputStream = null;
        String pathToDownload = String.format("%s/%s", globalConfiguration.getAnnotation().getFilesDirectory(), fileName);
        try {
            // download the document
            inputStream = new BufferedInputStream(new FileInputStream(pathToDownload));
            outStream = new BufferedOutputStream(out);
            while ((count = inputStream.read(buff)) != -1) {
                outStream.write(buff, 0, count);
            }
            return outStream;
        } catch (Exception ex){
            return generateException(response, ex);
        } finally {
            // close streams
            if (inputStream != null)
                inputStream.close();
            if (outStream != null)
                outStream.close();
        }
    }

    /**
     * Upload document
     * @param request
     * @param response
     * @return uploaded document object (the object contains uploaded document guid)
     */
    @POST
    @Path(value = "/uploadDocument")
    public Object uploadDocument(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        InputStream uploadedInputStream = null;
        try {
            // set multipart configuration
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement((String) null);
            request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfigElement);
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get the file chosen by the user
            Part filePart = request.getPart("file");
            // get document URL
            String documentUrl = request.getParameter("url");
            // get rewrite mode
            boolean rewrite = Boolean.parseBoolean(request.getParameter("rewrite"));
            String fileName;
            if(documentUrl == null || documentUrl.isEmpty()) {
                // get the InputStream to store the file
                uploadedInputStream = filePart.getInputStream();
                fileName = filePart.getSubmittedFileName();
            } else {
                // get the InputStream from the URL
                URL url =  new URL(documentUrl);
                uploadedInputStream = url.openStream();
                fileName = FilenameUtils.getName(url.getPath());
            }
            // get signatures storage path
            String documentStoragePath = globalConfiguration.getAnnotation().getFilesDirectory();
            // save the file
            String filePath =  String.format("%s/%s", documentStoragePath, fileName);
            File file = new File(filePath);
            // check rewrite mode
            if(rewrite) {
                // save file with rewrite if exists
                Files.copy(uploadedInputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                if (file.exists())
                {
                    // get file with new name
                    file = getFreeFileName(documentStoragePath, fileName);
                }
                // save file with out rewriting
                Files.copy(uploadedInputStream, file.toPath());
            }
            UploadedDocumentEntity uploadedDocument = new UploadedDocumentEntity();
            uploadedDocument.setGuid(documentStoragePath + "/" + fileName);
            return objectToJson(uploadedDocument);
        }catch(Exception ex){
            return generateException(response, ex);
        } finally {
            try {
                uploadedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get text coordinates
     * @param request
     * @param response
     * @return list of each text row with coordinates
     */
    @POST
    @Path(value = "/textCoordinates")
    public Object textCoordinates(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String password = "";
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            password = getJsonString(requestBody, "password");
            int pageNumber = getJsonInteger(requestBody, "pageNumber");
            // get document info
            DocumentInfoContainer info = annotationImageHandler.getDocumentInfo(new File(documentGuid).getName(), password);
            // get all rows info for specific page
            List<RowData> rows = info.getPages().get(pageNumber - 1).getRows();
            // initiate list of the TextRowEntity
            List<TextRowEntity> textCoordinates = new ArrayList<TextRowEntity>();
            // get each row info
            for(int i = 0; i < rows.size(); i++ ) {
                TextRowEntity textRow = new TextRowEntity();
                textRow.setTextCoordinates(info.getPages().get(pageNumber - 1).getRows().get(i).getCharacterCoordinates());
                textRow.setLineTop(info.getPages().get(pageNumber - 1).getRows().get(i).getLineTop());
                textRow.setLineHeight(info.getPages().get(pageNumber - 1).getRows().get(i).getLineHeight());
                textCoordinates.add(textRow);
            }
            return objectToJson(textCoordinates);
        }catch (Exception ex){
            return generateException(response, ex);
        }
    }

    /**
     * Annotate document
     * @param request
     * @param response
     * @return annotated document info
     */
    @POST
    @Path(value = "/annotate")
    public Object annotate(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String password = "";
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            password = getJsonString(requestBody, "password");
            AnnotationDataEntity[] annotationsData = (AnnotationDataEntity[]) getJsonObject(requestBody, "annotationsList", AnnotationDataEntity[].class);
            // initiate AnnotatedDocument object
            AnnotatedDocumentEntity annotatedDocument = new AnnotatedDocumentEntity();
            // initiate list of annotations to add
            List<AnnotationInfo> annotations = new ArrayList<AnnotationInfo>();
            // get document info - required to get document page height and calculate annotation top position
            DocumentInfoContainer info = annotationImageHandler.getDocumentInfo(new File(documentGuid).getName(), password);
            // check if document type is image
            if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                annotationsData[0].setDocumentType("image");
            }
            // initiate annotator object
            Annotator annotator = null;
            for(int i = 0; i < annotationsData.length; i++) {
                // create annotator
                annotator = getAnnotator(annotationsData[i], annotator);
                // add annotation
                addAnnotationOptions(annotationsData[0].getDocumentType(), info, annotations, annotator, annotationsData[i]);
            }
            InputStream result = null;
            // Add annotation to the document
            InputStream cleanDoc = new FileInputStream(documentGuid);
            switch (annotationsData[0].getDocumentType()) {
                case "Portable Document Format":
                    result = annotationImageHandler.exportAnnotationsToDocument(cleanDoc, annotations, DocumentType.Pdf);
                    break;
                case "Microsoft Word":
                    result = annotationImageHandler.exportAnnotationsToDocument(cleanDoc, annotations, DocumentType.Words);
                    break;
                case "Microsoft PowerPoint":
                    result = annotationImageHandler.exportAnnotationsToDocument(cleanDoc, annotations, DocumentType.Slides);
                    break;
                case "image":
                    result = annotationImageHandler.exportAnnotationsToDocument(cleanDoc, annotations, DocumentType.Images);
                    break;
                case "Microsoft Excel":
                    result = annotationImageHandler.exportAnnotationsToDocument(cleanDoc, annotations, DocumentType.Cells);
                    break;
                case "AutoCAD Drawing File Format":
                    result = annotationImageHandler.exportAnnotationsToDocument(cleanDoc, annotations, DocumentType.Diagram);
                    break;
            }
            // Save result stream to file.
            File outPut = new File(documentGuid);
            String path = globalConfiguration.getAnnotation().getOutputDirectory() + "/" + outPut.getName();
            OutputStream fileStream = new FileOutputStream(path);
            annotatedDocument.setGuid(path);
            IOUtils.copy(result, fileStream);
            fileStream.close();
            result.close();
            return objectToJson(annotatedDocument);
        }catch (Exception ex){
            return generateException(response, ex);
        }
    }

    private Annotator getAnnotator(AnnotationDataEntity annotationData, Annotator annotator) {
        switch (annotationData.getType()) {
            case "text":
                annotator = new TextAnnotator(annotationData);
                break;
            case "area":
                annotator = new AreaAnnotator(annotationData);
                break;
            case "point":
                annotator = new PointAnnotator(annotationData);
                break;
            case "textStrikeout":
                annotator = new TexStrikeoutAnnotator(annotationData);
                break;
            case "polyline":
                annotator = new PolylineAnnotator(annotationData);
                break;
        }
        return annotator;
    }

    /**
     * Add current signature options to signs collection
     * @param documentType
     * @param documentInfo
     * @param annotationsCollection
     * @param annotator
     * @param annotationData
     * @throws ParseException
     */
    private void addAnnotationOptions(String documentType, DocumentInfoContainer documentInfo, List<AnnotationInfo> annotationsCollection, Annotator annotator,  AnnotationDataEntity annotationData) throws ParseException {
        switch (documentType) {
            case "Portable Document Format":
                annotationsCollection.add(annotator.annotatePdf(documentInfo));
                break;
            case "Microsoft Word":
                for(int n = 0; n < annotationData.getComments().length; n++) {
                    annotationsCollection.add(annotator.annotateWord(documentInfo, annotationData.getComments()[n]));
                }
                break;
            case "Microsoft PowerPoint":
                annotationsCollection.add(annotator.annotateSlides(documentInfo));
                break;
            case "image":
                annotationsCollection.add(annotator.annotateImage(documentInfo));
                break;
            case "Microsoft Excel":
                for(int n = 0; n < annotationData.getComments().length; n++) {
                    annotationsCollection.add(annotator.annotateCells(documentInfo, annotationData.getComments()[n]));
                }
                break;
            case "AutoCAD Drawing File Format":
                annotationsCollection.add(annotator.annotateDiagram(documentInfo));
                break;
        }
    }
}