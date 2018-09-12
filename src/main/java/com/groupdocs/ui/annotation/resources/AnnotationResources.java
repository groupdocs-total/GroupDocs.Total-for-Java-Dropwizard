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
import com.groupdocs.ui.annotation.entity.request.AnnotateDocumentRequest;
import com.groupdocs.ui.annotation.entity.request.TextCoordinatesRequest;
import com.groupdocs.ui.annotation.entity.web.AnnotatedDocumentEntity;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.annotation.entity.web.TextRowEntity;
import com.groupdocs.ui.annotation.util.directory.DirectoryUtils;
import com.groupdocs.ui.annotation.views.Annotation;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.UploadedDocumentEntity;
import com.groupdocs.ui.common.entity.web.LoadedPageEntity;
import com.groupdocs.ui.common.entity.web.DocumentDescriptionEntity;
import com.groupdocs.ui.common.entity.web.request.FileTreeRequest;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentPageRequest;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;
import com.groupdocs.ui.common.exception.TotalGroupDocsException;
import com.groupdocs.ui.common.resources.Resources;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

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
     * @param fileTreeRequest request's object with specified path
     * @return files and directories list
     */
    @POST
    @Path(value = "/loadFileTree")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public List<FileDescriptionEntity> loadFileTree(FileTreeRequest fileTreeRequest) {
        String relDirPath = fileTreeRequest.getPath();
        // get file list from storage path
        FileTreeOptions fileListOptions = new FileTreeOptions(relDirPath);
        // get temp directory name
        String tempDirectoryName = new com.groupdocs.annotation.domain.config.AnnotationConfig().getTempFolderName();
        try {
            FileTreeContainer fileListContainer = annotationImageHandler.loadFileTree(fileListOptions);

            ArrayList<FileDescriptionEntity> fileList = new ArrayList<>();
            // parse files/folders list
            for (FileDescription fd : fileListContainer.getFileTree()) {
                FileDescriptionEntity fileDescription = new FileDescriptionEntity();
                fileDescription.setGuid(fd.getGuid());
                // check if current file/folder is temp directory or is hidden
                if (tempDirectoryName.toLowerCase().equals(fd.getName()) || new File(fileDescription.getGuid()).isHidden()) {
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
            return fileList;
        } catch (Exception ex) {
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Get document description
     * @return document description
     */
    @POST
    @Path(value = "/loadDocumentDescription")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public List<DocumentDescriptionEntity> loadDocumentDescription(LoadDocumentRequest loadDocumentRequest){
        try {
            // get/set parameters
            String documentGuid = loadDocumentRequest.getGuid();
            String password = loadDocumentRequest.getPassword();
            DocumentInfoContainer documentDescription;
            // get document info container
            documentDescription = annotationImageHandler.getDocumentInfo(new File(documentGuid).getName(), password);
            List<DocumentDescriptionEntity> pagesDescription = new ArrayList<>();
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
            return pagesDescription;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Get document page
     * @return document page
     */
    @POST
    @Path(value = "/loadDocumentPage")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public LoadedPageEntity loadDocumentPage(LoadDocumentPageRequest loadDocumentPageRequest){
        try {
            // get/set parameters
            String documentGuid = loadDocumentPageRequest.getGuid();
            int pageNumber = loadDocumentPageRequest.getPage();
            String password = loadDocumentPageRequest.getPassword();
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
            byte[] bytes = IOUtils.toByteArray(annotationImageHandler.getPages(new File(documentGuid).getName(), imageOptions).get(0).getStream());
            // encode ByteArray into String
            String incodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(incodedImage);
            // return loaded page object
            return loadedPage;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Download document
     * @param documentGuid path to document parameter
     * @param response
     */
    @GET
    @Path(value = "/downloadDocument")
    @Produces(APPLICATION_OCTET_STREAM)
    public void downloadDocument(@QueryParam("path") String documentGuid, @Context HttpServletResponse response) throws IOException {
        int count;
        byte[] buff = new byte[16 * 1024];
        OutputStream out = response.getOutputStream();
        String fileName = new File(documentGuid).getName();
        // set response content disposition
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        BufferedOutputStream outStream = null;
        BufferedInputStream inputStream = null;
        try {
            // download the document
            inputStream = new BufferedInputStream(new FileInputStream(documentGuid));
            outStream = new BufferedOutputStream(out);
            while ((count = inputStream.read(buff)) != -1) {
                outStream.write(buff, 0, count);
            }
        } catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
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
     * @param inputStream file content
     * @param fileDetail file description
     * @param documentUrl url for document
     * @param rewrite flag for rewriting file
     * @return uploaded document object (the object contains uploaded document guid)
     */
    @POST
    @Path(value = "/uploadDocument")
    @Produces(APPLICATION_JSON)
    @Consumes(MULTIPART_FORM_DATA)
    public UploadedDocumentEntity uploadDocument(@FormDataParam("file") InputStream inputStream,
                                                 @FormDataParam("file") FormDataContentDisposition fileDetail,
                                                 @FormDataParam("url") String documentUrl,
                                                 @FormDataParam("rewrite") Boolean rewrite) {
        InputStream uploadedInputStream = null;
        try {
            String fileName;
            if (StringUtils.isEmpty(documentUrl)) {
                // get the InputStream to store the file
                uploadedInputStream = inputStream;
                fileName = fileDetail.getFileName();
            } else {
                // get the InputStream from the URL
                URL url =  new URL(documentUrl);
                uploadedInputStream = url.openStream();
                fileName = FilenameUtils.getName(url.getPath());
            }
            // get documents storage path
            String documentStoragePath = globalConfiguration.getAnnotation().getFilesDirectory();
            // save the file
            File file = new File(documentStoragePath + File.separator + fileName);
            // check rewrite mode
            if(rewrite) {
                // save file with rewrite if exists
                Files.copy(uploadedInputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                if (file.exists()){
                    // get file with new name
                    file = getFreeFileName(documentStoragePath, fileName);
                }
                // save file with out rewriting
                Files.copy(uploadedInputStream, file.toPath());
            }
            UploadedDocumentEntity uploadedDocument = new UploadedDocumentEntity();
            uploadedDocument.setGuid(documentStoragePath + File.separator + fileName);
            return uploadedDocument;
        } catch(Exception ex) {
            throw new TotalGroupDocsException(ex.getMessage(), ex);
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
     * @param textCoordinatesRequest
     * @return list of each text row with coordinates
     */
    @POST
    @Path(value = "/textCoordinates")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public List<TextRowEntity> textCoordinates(TextCoordinatesRequest textCoordinatesRequest) {
        String password = "";
        try {
            // get/set parameters
            String documentGuid = textCoordinatesRequest.getGuid();
            password = textCoordinatesRequest.getPassword();
            int pageNumber = textCoordinatesRequest.getPageNumber();
            // get document info
            DocumentInfoContainer info = annotationImageHandler.getDocumentInfo(new File(documentGuid).getName(), password);
            // get all rows info for specific page
            List<RowData> rows = info.getPages().get(pageNumber - 1).getRows();
            // initiate list of the TextRowEntity
            List<TextRowEntity> textCoordinates = new ArrayList<TextRowEntity>();
            // get each row info
            for(int i = 0; i < rows.size(); i++ ) {
                TextRowEntity textRow = new TextRowEntity();
                textRow.setTextCoordinates(info.getPages().get(pageNumber - 1).getRows().get(i).getTextCoordinates());
                textRow.setLineTop(info.getPages().get(pageNumber - 1).getRows().get(i).getLineTop());
                textRow.setLineHeight(info.getPages().get(pageNumber - 1).getRows().get(i).getLineHeight());
                textCoordinates.add(textRow);
            }
            return textCoordinates;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Annotate document
     * @param annotateDocumentRequest
     * @return annotated document info
     */
    @POST
    @Path(value = "/annotate")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public AnnotatedDocumentEntity annotate(AnnotateDocumentRequest annotateDocumentRequest) {
        String password = "";
        Exception notSupportedException = null;
        try {
            // get/set parameters
            String documentGuid = annotateDocumentRequest.getGuid();
            password = annotateDocumentRequest.getPassword();
            AnnotationDataEntity[] annotationsData = annotateDocumentRequest.getAnnotationsData();
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
                try {
                    addAnnotationOptions(annotationsData[0].getDocumentType(), info, annotations, annotator, annotationsData[i]);
                } catch (Exception ex){
                    if(ex.getMessage().equals("Annotation of type " + annotationsData[i].getType() + " for this file type is not supported")){
                        notSupportedException = ex;
                        continue;
                    } else {
                        throw new TotalGroupDocsException(ex.getMessage(), ex);
                    }
                }
            }
            if(annotations.size() > 0) {
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
                return annotatedDocument;
            } else {
                throw new NotSupportedException(notSupportedException.getMessage(), notSupportedException);
            }
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
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
            case "textField":
                annotator = new TextFieldAnnotator(annotationData);
                break;
            case "watermark":
                annotator = new WatermarkAnnotator(annotationData);
                break;
            case "textReplacement":
                annotator = new TextReplacementAnnotator(annotationData);
                break;
            case "arrow":
                annotator = new ArrowAnnotator(annotationData);
                break;
            case "textRedaction":
                annotator = new TextRedactionAnnotator(annotationData);
                break;
            case "resourcesRedaction":
                annotator = new ResourceRedactionAnnotator(annotationData);
                break;
            case "textUnderline":
                annotator = new TexUnderlineAnnotator(annotationData);
                break;
            case "distance":
                annotator = new DistanceAnnotator(annotationData);
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
                if(annotationData.getComments().length == 0){
                    annotationsCollection.add(annotator.annotateWord(documentInfo, null));
                } else {
                    for (int n = 0; n < annotationData.getComments().length; n++) {
                        annotationsCollection.add(annotator.annotateWord(documentInfo, annotationData.getComments()[n]));
                    }
                }
                break;
            case "Microsoft PowerPoint":
                annotationsCollection.add(annotator.annotateSlides(documentInfo));
                break;
            case "image":
                annotationsCollection.add(annotator.annotateImage(documentInfo));
                break;
            case "Microsoft Excel":
                if(annotationData.getComments().length == 0){
                    annotationsCollection.add(annotator.annotateCells(documentInfo, null));
                } else {
                    for (int n = 0; n < annotationData.getComments().length; n++) {
                        annotationsCollection.add(annotator.annotateCells(documentInfo, annotationData.getComments()[n]));
                    }
                }
                break;
            case "AutoCAD Drawing File Format":
                annotationsCollection.add(annotator.annotateDiagram(documentInfo));
                break;
        }
    }
}