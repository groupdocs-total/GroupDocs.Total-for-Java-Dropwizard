package com.groupdocs.ui.annotation.resources;

import com.groupdocs.annotation.common.license.License;
import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.FileDescription;
import com.groupdocs.annotation.domain.RowData;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.annotation.domain.containers.FileTreeContainer;
import com.groupdocs.annotation.domain.image.PageImage;
import com.groupdocs.annotation.domain.options.FileTreeOptions;
import com.groupdocs.annotation.domain.options.ImageOptions;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.ui.annotation.annotator.*;
import com.groupdocs.ui.annotation.entity.request.AnnotateDocumentRequest;
import com.groupdocs.ui.annotation.entity.request.TextCoordinatesRequest;
import com.groupdocs.ui.annotation.entity.web.*;
import com.groupdocs.ui.annotation.importer.Importer;
import com.groupdocs.ui.annotation.util.directory.DirectoryUtils;
import com.groupdocs.ui.annotation.views.Annotation;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.LoadedPageEntity;
import com.groupdocs.ui.common.entity.web.UploadedDocumentEntity;
import com.groupdocs.ui.common.entity.web.request.FileTreeRequest;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentPageRequest;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;
import com.groupdocs.ui.common.exception.TotalGroupDocsException;
import com.groupdocs.ui.common.resources.Resources;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.*;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.groupdocs.ui.annotation.util.DocumentTypesConverter.getDocumentType;
import static javax.ws.rs.core.MediaType.*;

/**
 * AnnotationResources
 * @author Aspose Pty Ltd
 */

@Path(value = "/annotation")
public class AnnotationResources extends Resources {
    private final AnnotationImageHandler annotationImageHandler;
    private DirectoryUtils directoryUtils;
    private String[] supportedImageFormats = {"bmp", "jpeg", "jpg", "tiff", "tif", "png", "gif", "emf", "wmf", "dwg", "dicom", "djvu"};
    private String[] supportedAutoCadFormats = {"dxf", "dwg"};

    /**
     * Constructor
     * @param globalConfiguration global configuration object
     * @throws UnknownHostException
     */
    public AnnotationResources(GlobalConfiguration globalConfiguration) throws UnknownHostException {
        super(globalConfiguration);

        // create annotation directories
        directoryUtils = new DirectoryUtils(globalConfiguration.getAnnotation());

        // create annotation application configuration
        AnnotationConfig config = new AnnotationConfig();
        // set storage path
        config.setStoragePath(directoryUtils.getFilesDirectory().getPath());
        // set directory to store annotated documents
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
    public List<AnnotatedDocumentEntity> loadDocumentDescription(LoadDocumentRequest loadDocumentRequest){
        try {
            // get/set parameters
            String documentGuid = loadDocumentRequest.getGuid();
            String password = loadDocumentRequest.getPassword();
            DocumentInfoContainer documentDescription;
            // get document info container
            String fileName = FilenameUtils.getName(documentGuid);
            documentDescription = annotationImageHandler.getDocumentInfo(fileName, password);

            String documentType = documentDescription.getDocumentType();
            // check if document type is image
            if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                documentType = "image";
            } else if (Arrays.asList(supportedAutoCadFormats).contains(FilenameUtils.getExtension(documentGuid))){
                documentType = "diagram";
            }
            // check if document contains annotations
            AnnotationInfo[] annotations = getAnnotations(documentGuid, documentType);
            // initiate pages description list
            List<AnnotatedDocumentEntity> pagesDescription = new ArrayList<>();
            // get info about each document page
            for(int i = 0; i < documentDescription.getPages().size(); i++) {
                //initiate custom Document description object
                AnnotatedDocumentEntity description = new AnnotatedDocumentEntity();
                // set current page info for result
                description.setHeight(documentDescription.getPages().get(i).getHeight());
                description.setWidth(documentDescription.getPages().get(i).getWidth());
                description.setNumber(documentDescription.getPages().get(i).getNumber());
                // set annotations data if document page contains annotations
                if(annotations != null && annotations.length > 0) {
                   description.setAnnotations(setAnnotations(annotations, description.getNumber()));
                }
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
            InputStream document = new FileInputStream(documentGuid);
            List<PageImage> images = annotationImageHandler.getPages(document, imageOptions);

            byte[] bytes = IOUtils.toByteArray(images.get(pageNumber - 1).getStream());
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
     * @param annotated
     * @param response
     */
    @GET
    @Path(value = "/downloadDocument")
    @Produces(APPLICATION_OCTET_STREAM)
    public void downloadDocument(@QueryParam("path") String documentGuid,
                                 @QueryParam("annotated") Boolean annotated,
                                 @Context HttpServletResponse response) throws IOException {
        // get document path
        String fileName = new File(documentGuid).getName();
        // choose directory
        String directory = annotated ? directoryUtils.getOutputDirectory().getPath() : directoryUtils.getFilesDirectory().getPath();
        String pathToDownload = String.format("%s%s%s", directory, File.separator, fileName);
        // download the file
        downloadFile(response, documentGuid, pathToDownload);
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
        // upload file
        String pathname = uploadFile(documentUrl, inputStream, fileDetail, rewrite, null);
        // create response
        UploadedDocumentEntity uploadedDocument = new UploadedDocumentEntity();
        uploadedDocument.setGuid(pathname);
        return uploadedDocument;

    }

    @Override
    protected String getStoragePath(Map<String, Object> params) {
        return globalConfiguration.getAnnotation().getFilesDirectory();
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
        AnnotatedDocumentEntity annotatedDocument = new AnnotatedDocumentEntity();
        try {
            // get/set parameters
            String documentGuid = annotateDocumentRequest.getGuid();
            String password = annotateDocumentRequest.getPassword();
            AnnotationDataEntity[] annotationsData = annotateDocumentRequest.getAnnotationsData();
            // initiate AnnotatedDocument object
            // initiate list of annotations to add
            List<AnnotationInfo> annotations = new ArrayList<AnnotationInfo>();
            // get document info - required to get document page height and calculate annotation top position
            DocumentInfoContainer documentInfo = annotationImageHandler.getDocumentInfo(new File(documentGuid).getName(), password);
            // check if document type is image
            if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                annotationsData[0].setDocumentType("image");
            }
            // initiate annotator object
            Annotator annotator = null;
            String documentType = annotationsData[0].getDocumentType();
            Exception notSupportedException = null;
            for(int i = 0; i < annotationsData.length; i++) {
                // create annotator
                annotator = getAnnotator(annotationsData[i], annotator, documentInfo);
                // add annotation, if current annotation type isn't supported by the current document type it will be ignored
                try {
                    addAnnotationOptions(documentType, annotations, annotator);
                } catch (Exception ex){
                    if(ex.getMessage().equals("Annotation of type " + annotationsData[i].getType() + " for this file type is not supported")){
                        notSupportedException = ex;
                        continue;
                    } else {
                        throw new TotalGroupDocsException(ex.getMessage(), ex);
                    }
                }
            }
            // check if annotations array contains at least one annotation to add
            if(annotations.size() > 0) {
                // Add annotation to the document
                int type = getDocumentType(documentType);
                InputStream cleanDoc = new FileInputStream(documentGuid);
                InputStream result = annotationImageHandler.exportAnnotationsToDocument(cleanDoc, annotations, type);
                // Save result stream to file.
                File outPut = new File(documentGuid);
                String path = globalConfiguration.getAnnotation().getOutputDirectory() + File.separator + outPut.getName();
                OutputStream fileStream = new FileOutputStream(path);
                annotatedDocument.setGuid(path);
                IOUtils.copy(result, fileStream);
                fileStream.close();
                result.close();
            } else if (notSupportedException != null) {
                throw new NotSupportedException(notSupportedException.getMessage(), notSupportedException);
            }
        } catch (Exception ex) {
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
        return annotatedDocument;
    }

    /**
     * get annotator object
     * @param annotationData
     * @param annotator
     * @param documentInfo
     * @return annotator object
     */
    private Annotator getAnnotator(AnnotationDataEntity annotationData, Annotator annotator, DocumentInfoContainer documentInfo) {
        switch (annotationData.getType()) {
            case "text":
                annotator = new TextAnnotator(annotationData, documentInfo);
                break;
            case "area":
                annotator = new AreaAnnotator(annotationData, documentInfo);
                break;
            case "point":
                annotator = new PointAnnotator(annotationData, documentInfo);
                break;
            case "textStrikeout":
                annotator = new TexStrikeoutAnnotator(annotationData, documentInfo);
                break;
            case "polyline":
                annotator = new PolylineAnnotator(annotationData, documentInfo);
                break;
            case "textField":
                annotator = new TextFieldAnnotator(annotationData, documentInfo);
                break;
            case "watermark":
                annotator = new WatermarkAnnotator(annotationData, documentInfo);
                break;
            case "textReplacement":
                annotator = new TextReplacementAnnotator(annotationData, documentInfo);
                break;
            case "arrow":
                annotator = new ArrowAnnotator(annotationData, documentInfo);
                break;
            case "textRedaction":
                annotator = new TextRedactionAnnotator(annotationData, documentInfo);
                break;
            case "resourcesRedaction":
                annotator = new ResourceRedactionAnnotator(annotationData, documentInfo);
                break;
            case "textUnderline":
                annotator = new TexUnderlineAnnotator(annotationData, documentInfo);
                break;
            case "distance":
                annotator = new DistanceAnnotator(annotationData, documentInfo);
                break;
        }
        return annotator;
    }

    /**
     * Add current annotation options to annotations collection
     * @param documentType
     * @param annotationsCollection
     * @param annotator
     * @throws ParseException
     */
    private void addAnnotationOptions(String documentType, List<AnnotationInfo> annotationsCollection, Annotator annotator) throws ParseException {
        switch (documentType) {
            case "Portable Document Format":
                annotationsCollection.add(annotator.annotatePdf());
                break;
            case "Microsoft Word":
                annotationsCollection.add(annotator.annotateWord());
                break;
            case "Microsoft PowerPoint":
                annotationsCollection.add(annotator.annotateSlides());
                break;
            case "image":
                annotationsCollection.add(annotator.annotateImage());
                break;
            case "Microsoft Excel":
                annotationsCollection.add(annotator.annotateCells());
                break;
            case "AutoCAD Drawing File Format":
                annotationsCollection.add(annotator.annotateDiagram());
                break;
        }
    }

    /**
     * Get all annotations from the document
     *
     * @param documentGuid
     * @param documentType
     * @return array of the annotations
     */
    private AnnotationInfo[] getAnnotations(String documentGuid, String documentType) {
        try {
            FileInputStream documentStream = new FileInputStream(documentGuid);
            int docType = getDocumentType(documentType);
            return new Importer(documentStream, annotationImageHandler).importAnnotations(docType);
        } catch (Exception ex) {
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * set all imported annotations data
     * @param annotations
     * @param pageNumber
     * @return annotations data entity array
     */
    private AnnotationDataEntity[] setAnnotations(AnnotationInfo[] annotations, int pageNumber){
        // initiate annotations data array
        AnnotationDataEntity[] pageAnnotations = new AnnotationDataEntity[annotations.length];
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        // set each annotation data - this functionality used since annotations data returned by the
        // GroupDocs.Annotation library are obfuscated
        for (int n = 0; n < annotations.length; n++){
            if(pageNumber == annotations[n].getPageNumber() + 1) {
                AnnotationDataEntity annotation = new AnnotationDataEntity();
                annotation.setFont(annotations[n].getFontFamily());
                annotation.setFontSize(annotations[n].getFontSize());
                annotation.setHeight(annotations[n].getBox().getHeight());
                annotation.setLeft(annotations[n].getBox().getX());
                annotation.setPageNumber(annotations[n].getPageNumber() + 1);
                annotation.setSvgPath(annotations[n].getSvgPath());
                String text = (annotations[n].getText() == null) ? annotations[n].getFieldText() : annotations[n].getText();
                annotation.setText(text);
                annotation.setTop(annotations[n].getBox().getY());
                AnnotationTypes annotationTypes = new AnnotationTypes();
                annotation.setType(annotationTypes.getAnnotationType(annotations[n].getType()));
                annotation.setWidth(annotations[n].getBox().getWidth());
                // set each creply data
                if (annotations[n].getReplies() != null && annotations[n].getReplies().length > 0) {
                    CommentsEntity[] comments = new CommentsEntity[annotations[n].getReplies().length];
                    for (int m = 0; m < annotations[n].getReplies().length; m++) {
                        CommentsEntity comment = new CommentsEntity();
                        comment.setText(annotations[n].getReplies()[m].getMessage());
                        comment.setTime(format.format(annotations[n].getReplies()[m].getRepliedOn()));
                        comment.setUserName(annotations[n].getReplies()[m].getUserName());
                        comments[m] = comment;
                    }
                    annotation.setComments(comments);
                }
                pageAnnotations[n] = annotation;
            }
        }
        return pageAnnotations;
    }
}