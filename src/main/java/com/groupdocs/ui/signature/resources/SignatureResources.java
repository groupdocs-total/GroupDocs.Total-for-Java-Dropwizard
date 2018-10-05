package com.groupdocs.ui.signature.resources;

import com.google.common.collect.Lists;
import com.groupdocs.signature.config.SignatureConfig;
import com.groupdocs.signature.domain.DocumentDescription;
import com.groupdocs.signature.handler.SignatureHandler;
import com.groupdocs.signature.licensing.License;
import com.groupdocs.signature.options.OutputType;
import com.groupdocs.signature.options.SignatureOptionsCollection;
import com.groupdocs.signature.options.loadoptions.LoadOptions;
import com.groupdocs.signature.options.saveoptions.SaveOptions;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.DocumentDescriptionEntity;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.LoadedPageEntity;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentPageRequest;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;
import com.groupdocs.ui.common.exception.TotalGroupDocsException;
import com.groupdocs.ui.common.resources.Resources;
import com.groupdocs.ui.signature.entity.directory.BarcodeDataDirectoryEntity;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;
import com.groupdocs.ui.signature.entity.directory.QrCodeDataDirectoryEntity;
import com.groupdocs.ui.signature.entity.request.*;
import com.groupdocs.ui.signature.entity.web.SignatureDataEntity;
import com.groupdocs.ui.signature.entity.web.SignatureFileDescriptionEntity;
import com.groupdocs.ui.signature.entity.web.SignedDocumentEntity;
import com.groupdocs.ui.signature.entity.xml.OpticalXmlEntity;
import com.groupdocs.ui.signature.entity.xml.StampXmlEntity;
import com.groupdocs.ui.signature.entity.xml.StampXmlEntityList;
import com.groupdocs.ui.signature.entity.xml.TextXmlEntity;
import com.groupdocs.ui.signature.signatureloader.SignatureLoader;
import com.groupdocs.ui.signature.signer.*;
import com.groupdocs.ui.signature.util.XMLReaderWriter;
import com.groupdocs.ui.signature.util.directory.DirectoryUtils;
import com.groupdocs.ui.signature.views.Signature;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import static javax.ws.rs.core.MediaType.*;

/**
 * SignatureResources
 *
 * @author Aspose Pty Ltd
 */

@Path(value = "/signature")
public class SignatureResources extends Resources {

    private static final Logger logger = LoggerFactory.getLogger(SignatureResources.class);

    public static final String SIGNATURE_TYPE_PARAM = "signatureType";
    private final SignatureHandler signatureHandler;
    private DirectoryUtils directoryUtils;
    private List<String> supportedImageFormats = Arrays.asList("bmp", "jpeg", "jpg", "tiff", "tif", "png");

    /**
     * Constructor
     * @param globalConfiguration global configuration object
     * @throws UnknownHostException
     */
    public SignatureResources(GlobalConfiguration globalConfiguration) throws UnknownHostException {
        super(globalConfiguration);

        directoryUtils = new DirectoryUtils(globalConfiguration.getSignature());

        // create signature application configuration
        SignatureConfig config = new SignatureConfig();
        config.setStoragePath(directoryUtils.getFilesDirectory().getPath());
        config.setCertificatesPath(directoryUtils.getDataDirectory().getCertificateDirectory().getPath());
        config.setImagesPath(directoryUtils.getDataDirectory().getImageDirectory().getPath());
        config.setOutputPath(directoryUtils.getOutputDirectory().getPath());

        try {
            // set GroupDocs license
            License license = new License();
            license.setLicense(globalConfiguration.getApplication().getLicensePath());
        } catch (Throwable throwable) {
            logger.error("Can not verify Signature license!");
        }
        // initialize total instance for the Image mode
        signatureHandler = new SignatureHandler(config);
    }

    /**
     * Get and set signature page
     * @return html view
     */
    @GET
    public Signature getView(){
        // initiate index page
        return new Signature(globalConfiguration, DEFAULT_CHARSET);
    }

    /**
     * Get files and directories
     */
    @POST
    @Path(value = "/loadFileTree")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public List<SignatureFileDescriptionEntity> loadFileTree(SignatureFileTreeRequest signatureFileTreeRequest){
        String relDirPath = signatureFileTreeRequest.getPath();
        String signatureType = signatureFileTreeRequest.getSignatureType() == null ? "" : signatureFileTreeRequest.getSignatureType();
        // get file list from storage path
        try{
            String rootDirectory;
            switch (signatureType) {
                case "digital":  rootDirectory = directoryUtils.getDataDirectory().getCertificateDirectory().getPath();
                    break;
                case "image": rootDirectory = directoryUtils.getDataDirectory().getImageDirectory().getPath();
                    break;
                case "stamp": rootDirectory = directoryUtils.getDataDirectory().getStampDirectory().getPath();
                    break;
                case "text": rootDirectory = directoryUtils.getDataDirectory().getTextDirectory().getPath();
                    break;
                default:  rootDirectory = directoryUtils.getFilesDirectory().getPath();
                    break;
            }
            // get all the files from a directory
            if (StringUtils.isEmpty(relDirPath)) {
                relDirPath = rootDirectory;
            } else {
                relDirPath = String.format("%s%s%s", rootDirectory, File.separator, relDirPath);
            }
            SignatureLoader signatureLoader = new SignatureLoader(relDirPath, globalConfiguration);
            List<SignatureFileDescriptionEntity> fileList;
            switch (signatureType) {
                case "digital":  fileList = signatureLoader.loadFiles();
                    break;
                case "image": fileList = signatureLoader.loadImageSignatures();
                    break;
                case "stamp": fileList = signatureLoader.loadStampSignatures(DataDirectoryEntity.DATA_PREVIEW_FOLDER, DataDirectoryEntity.DATA_XML_FOLDER);
                    break;
                case "text": fileList = signatureLoader.loadStampSignatures(DataDirectoryEntity.DATA_PREVIEW_FOLDER, DataDirectoryEntity.DATA_XML_FOLDER);
                    break;
                default:  fileList = signatureLoader.loadFiles();
                    break;
            }
            return fileList;
        }catch (Exception ex){
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
            DocumentDescription documentDescription;
            // get document info container
            documentDescription = signatureHandler.getDocumentDescription(documentGuid, password);
            List<DocumentDescriptionEntity> pagesDescription = new ArrayList<>();
            // get info about each document page
            for(int i = 1; i <= documentDescription.getPageCount(); i++) {
                //initiate custom Document description object
                DocumentDescriptionEntity description = new DocumentDescriptionEntity();
                // get current page size
                java.awt.Dimension pageSize = signatureHandler.getDocumentPageSize(documentGuid, i, password, (double)0, (double)0, null);
                // set current page info for result
                description.setHeight(pageSize.getHeight());
                description.setWidth(pageSize.getWidth());
                description.setNumber(i);
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
            // get page image
            byte[] bytes = signatureHandler.getPageImage(documentGuid, pageNumber, password, null, 100);
            // encode ByteArray into String
            String encodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(encodedImage);
            // return loaded page object
            return loadedPage;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Download document
     * @return document
     */
    @GET
    @Path(value = "/downloadDocument")
    @Produces(APPLICATION_OCTET_STREAM)
    public void downloadDocument(@QueryParam("path") String documentGuid,
                                 @QueryParam("signed") Boolean signed,
                                 @Context HttpServletResponse response) throws IOException {
        // get document path
        String fileName = FilenameUtils.getName(documentGuid);
        // choose directory
        String directory = signed ? directoryUtils.getOutputDirectory().getPath() : directoryUtils.getFilesDirectory().getPath();
        String pathToDownload = String.format("%s%s%s", directory, File.separator, fileName);

        downloadFile(response, pathToDownload);
    }

    /**
     * Upload document
     * @return uploaded document object (the object contains uploaded document guid)
     */
    @POST
    @Path(value = "/uploadDocument")
    @Produces(APPLICATION_JSON)
    @Consumes(MULTIPART_FORM_DATA)
    public SignatureFileDescriptionEntity uploadDocument(@FormDataParam("file") InputStream inputStream,
                                                         @FormDataParam("file") FormDataContentDisposition fileDetail,
                                                         @FormDataParam("url") String documentUrl,
                                                         @FormDataParam("rewrite") Boolean rewrite,
                                                         @FormDataParam("signatureType") String signatureType) {
        if (signatureType == null) {
            signatureType = "";
        }
        Map<String, Object> params = new HashMap<>();
        params.put(SIGNATURE_TYPE_PARAM, signatureType);
        // upload file
        String filePath = uploadFile(documentUrl, inputStream, fileDetail, rewrite, params);
        // create response
        SignatureFileDescriptionEntity uploadedDocument = new SignatureFileDescriptionEntity();
        uploadedDocument.setGuid(filePath);
        if("image".equals(signatureType)){
            // get page image
            try {
                byte[] bytes = Files.readAllBytes(new File(uploadedDocument.getGuid()).toPath());
                // encode ByteArray into String
                String encodedImage = new String(Base64.getEncoder().encode(bytes));
                uploadedDocument.setImage(encodedImage);
            } catch (IOException ex) {
                throw new TotalGroupDocsException(ex.getMessage(), ex);
            }
        }
        return uploadedDocument;
    }

    @Override
    protected String getStoragePath(Map<String, Object> params) {
        String signatureType = (String) params.get(SIGNATURE_TYPE_PARAM);
        String documentStoragePath;
        if (signatureType == null || signatureType.isEmpty()) {
            signatureType = "";
        }
        switch(signatureType){
            case "digital": documentStoragePath = directoryUtils.getDataDirectory().getCertificateDirectory().getPath();
                break;
            case "image": documentStoragePath = directoryUtils.getDataDirectory().getImageDirectory().getPath();
                break;
            default:  documentStoragePath = directoryUtils.getFilesDirectory().getPath();
                break;
        }
        return documentStoragePath;
    }

    /**
     * Get signature image stream - temporarlly workaround used until release of the GroupDocs.Signature 18.5, after release will be removed
     * @return signature image
     */
    @POST
    @Path(value = "/loadSignatureImage")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public LoadedPageEntity loadSignatureImage(LoadSignatureImageRequest loadSignatureImageRequest){
        try {
            // get/set parameters
            String documentGuid = loadSignatureImageRequest.getGuid();
            LoadedPageEntity loadedPage = new LoadedPageEntity();
            // get page image
            byte[] bytes = Files.readAllBytes( new File(documentGuid).toPath());
            // encode ByteArray into String
            String encodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(encodedImage);
            // return loaded page object
            return loadedPage;
        } catch (Exception ex) {
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Sign document with digital signature
     * @return signed document info
     */
    @POST
    @Path(value = "/signDigital")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public SignedDocumentEntity signDigital(SignDocumentRequest signDocumentRequest){
        try {
            // get/set parameters
            String documentGuid = signDocumentRequest.getGuid();
            String password = signDocumentRequest.getPassword();
            List<SignatureDataEntity> signaturesData = signDocumentRequest.getSignaturesData();
            if (signaturesData == null || signaturesData.isEmpty()) {
                throw new IllegalArgumentException("Sign is empty");
            }
            // get signed document name
            String signedFileName = new File(documentGuid).getName();
            // initiate signed document wrapper
            SignedDocumentEntity signedDocument = new SignedDocumentEntity();

            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(signedFileName);

            LoadOptions loadOptions = new LoadOptions();
            if (StringUtils.isNotEmpty(password)) {
                loadOptions.setPassword(password);
            }
            // initiate digital signer
            DigitalSigner signer = new DigitalSigner(signaturesData.get(0), password);
            // prepare signing options and sign document
            String documentType = signaturesData.get(0).getDocumentType();
            switch (documentType){
                case "Portable Document Format":
                    // sign document
                    signedDocument.setGuid(signatureHandler.sign(documentGuid, signer.signPdf(), loadOptions, saveOptions).toString());
                    break;
                case "Microsoft Word":
                    // sign document
                    signedDocument.setGuid(signatureHandler.sign(documentGuid, signer.signWord(), loadOptions, saveOptions).toString());
                    break;
                case "Microsoft Excel":
                    // sign document
                    signedDocument.setGuid(signatureHandler.sign(documentGuid, signer.signCells(), loadOptions, saveOptions).toString());
                    break;
                default:
                    throw new IllegalStateException(String.format("File format %s is not supported.", documentType));
            }
            // return loaded page object
            return signedDocument;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Sign document with image signature
     * @return signed document info
     */
    @POST
    @Path(value = "/signImage")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public SignedDocumentEntity signImage(SignDocumentRequest signDocumentRequest){
        try {
            // get/set parameters
            String documentGuid = signDocumentRequest.getGuid();
            String password = signDocumentRequest.getPassword();
            List<SignatureDataEntity> signaturesData = signDocumentRequest.getSignaturesData();
            if (signaturesData == null || signaturesData.isEmpty()) {
                throw new IllegalArgumentException("Sign is empty");
            }

            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // set signature password if required
            for(int i = 0; i < signaturesData.size(); i++) {
                SignatureDataEntity signatureDataEntity = signaturesData.get(i);
                if(signatureDataEntity.getDeleted()){
                    continue;
                } else {
                    // check if document type is image
                    if (supportedImageFormats.contains(FilenameUtils.getExtension(documentGuid))) {
                        signatureDataEntity.setDocumentType("image");
                    }
                    // initiate image signer object
                    ImageSigner signer = new ImageSigner(signatureDataEntity);
                    // prepare signing options and sign document
                    addSignOptions(signatureDataEntity.getDocumentType(), signsCollection, signer);
                }
            }
            // return loaded page object
            return signDocument(documentGuid, password, signsCollection);
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Sign document with stamp signature
     * @return signed document info
     */
    @POST
    @Path(value = "/signStamp")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public SignedDocumentEntity signStamp(SignDocumentRequest signDocumentRequest){
        String xmlPath = directoryUtils.getDataDirectory().getStampDirectory().getXmlPath();
        try {
            // get/set parameters
            String documentGuid = signDocumentRequest.getGuid();
            String password = signDocumentRequest.getPassword();
            List<SignatureDataEntity> signaturesData = signDocumentRequest.getSignaturesData();
            if (signaturesData == null || signaturesData.isEmpty()) {
                throw new IllegalArgumentException("Sign is empty");
            }

            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // mimeType should now be something like "image/png" if the document is image
            if (supportedImageFormats.contains(FilenameUtils.getExtension(documentGuid))) {
                signaturesData.get(0).setDocumentType("image");
            }

            for(int i = 0; i < signaturesData.size(); i++) {
                SignatureDataEntity signatureDataEntity = signaturesData.get(i);
                if(signatureDataEntity.getDeleted()){
                    continue;
                } else {
                    String xmlFileName = FilenameUtils.removeExtension(new File(signatureDataEntity.getSignatureGuid()).getName());
                    // Load xml data
                    String fileName = String.format("%s%s%s.xml", xmlPath, File.separator, xmlFileName);
                    StampXmlEntityList stampData;
                    try {
                        // try to read new xml format
                        stampData = new XMLReaderWriter<StampXmlEntityList>().read(fileName, StampXmlEntityList.class);
                    } catch (Exception ex) {
                        // if exception, try to read old xml format
                        stampData = new StampXmlEntityList();
                        StampXmlEntity[] stampDataArray = (StampXmlEntity[]) loadXmlData(fileName);
                        stampData.setStampXmlEntityList(Arrays.asList(stampDataArray));
                    }
                    // since stamp ine are added stating from the most outer line we need to reverse the stamp data array
                    List<StampXmlEntity> reverse = Lists.reverse(stampData.getStampXmlEntityList());
                    // initiate stamp signer
                    StampSigner signer = new StampSigner(reverse, signatureDataEntity);
                    // prepare signing options and sign document
                    addSignOptions(signatureDataEntity.getDocumentType(), signsCollection, signer);
                }
            }
            // return loaded page object
            return signDocument(documentGuid, password, signsCollection);
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Sign document with Optical signature
     * @return signed document info
     */
    @POST
    @Path(value = "/signOptical")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public SignedDocumentEntity signOptical(SignDocumentRequest signDocumentRequest){
        try {
            // get/set parameters
            String documentGuid = signDocumentRequest.getGuid();
            String password = signDocumentRequest.getPassword();
            List<SignatureDataEntity> signaturesData = signDocumentRequest.getSignaturesData();
            if (signaturesData == null || signaturesData.isEmpty()) {
                throw new IllegalArgumentException("Sign is empty");
            }
            String signatureType = signaturesData.get(0).getSignatureType();

            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // is qrCode
            boolean qrCode = signatureType.equals("qrCode");
            // get xml files root path
            String xmlPath = qrCode ? directoryUtils.getDataDirectory().getQrCodeDirectory().getXmlPath() : directoryUtils.getDataDirectory().getBarcodeDirectory().getXmlPath();
            // prepare signing options and sign document
            for(int i = 0; i < signaturesData.size(); i++) {
                if(signaturesData.get(i).getDeleted()){
                    continue;
                } else {
                    // get xml data of the QR-Code
                    String xmlFileName = FilenameUtils.removeExtension(new File(signaturesData.get(i).getSignatureGuid()).getName());
                    // Load xml data
                    String fileName = String.format("%s%s%s.xml", xmlPath, File.separator, xmlFileName);
                    OpticalXmlEntity opticalCodeData;
                    try {
                        // try to read new xml format
                        opticalCodeData = new XMLReaderWriter<OpticalXmlEntity>().read(fileName, OpticalXmlEntity.class);
                    } catch(Exception ex) {
                        // if exception, try to read old xml format
                        opticalCodeData = (OpticalXmlEntity) loadXmlData(fileName);
                    }
                    // check if document type is image
                    if (supportedImageFormats.contains(FilenameUtils.getExtension(documentGuid))) {
                        signaturesData.get(i).setDocumentType("image");
                    }
                    // initiate QRCode signer object
                    Signer signer = qrCode ? new QrCodeSigner(opticalCodeData, signaturesData.get(i)) : new BarCodeSigner(opticalCodeData, signaturesData.get(i));
                    // prepare signing options and sign document
                    addSignOptions(signaturesData.get(i).getDocumentType(), signsCollection, signer);
                }
            }
            // return loaded page object
            return signDocument(documentGuid, password, signsCollection);
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Sign document with Text signature
     * @return signed document info
     */
    @POST
    @Path(value = "/signText")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public SignedDocumentEntity signText(SignDocumentRequest signDocumentRequest){
        String xmlPath = directoryUtils.getDataDirectory().getTextDirectory().getXmlPath();
        try {
            // get/set parameters
            String documentGuid = signDocumentRequest.getGuid();
            String password = signDocumentRequest.getPassword();
            List<SignatureDataEntity> signaturesData = signDocumentRequest.getSignaturesData();
            if (signaturesData == null || signaturesData.isEmpty()) {
                throw new IllegalArgumentException("Sign is empty");
            }

            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // prepare signing options and sign document
            for(int i = 0; i < signaturesData.size(); i++) {
                if(signaturesData.get(i).getDeleted()){
                    continue;
                } else {
                    // get xml data of the Text signature
                    String xmlFileName = FilenameUtils.removeExtension(new File(signaturesData.get(i).getSignatureGuid()).getName());
                    // Load xml data
                    String fileName = String.format("%s%s%s.xml", xmlPath, File.separator, xmlFileName);
                    TextXmlEntity textData;
                    try {
                        // try to read new xml format
                        textData = new XMLReaderWriter<TextXmlEntity>().read(fileName, TextXmlEntity.class);
                    } catch(Exception ex) {
                        // if exception, try to read old xml format
                        textData = (TextXmlEntity) loadXmlData(fileName);
                    }
                    // check if document type is image
                    if (supportedImageFormats.contains(FilenameUtils.getExtension(documentGuid))) {
                        signaturesData.get(i).setDocumentType("image");
                    }
                    // initiate QRCode signer object
                    TextSigner signer = new TextSigner(textData, signaturesData.get(i));
                    // prepare signing options and sign document
                    addSignOptions(signaturesData.get(i).getDocumentType(), signsCollection, signer);
                }
            }
            // return loaded page object
            return signDocument(documentGuid, password, signsCollection);
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Add current signature options to signs collection
     * @param documentType
     * @param signsCollection
     * @param signer
     * @throws ParseException
     */
    private void addSignOptions(String documentType, SignatureOptionsCollection signsCollection, Signer signer) throws ParseException {
        switch (documentType) {
            case "Portable Document Format":
                signsCollection.add(signer.signPdf());
                break;
            case "Microsoft Word":
                signsCollection.add(signer.signWord());
                break;
            case "Microsoft PowerPoint":
                signsCollection.add(signer.signSlides());
                break;
            case "image":
                signsCollection.add(signer.signImage());
                break;
            case "Microsoft Excel":
                signsCollection.add(signer.signCells());
                break;
        }
    }

    /**
     * Sign document
     * @param documentGuid
     * @param password
     * @param signsCollection
     * @return signed document
     * @throws Exception
     */
    private SignedDocumentEntity signDocument(String documentGuid, String password, SignatureOptionsCollection signsCollection) throws Exception {
        // set save options
        final SaveOptions saveOptions = new SaveOptions();
        saveOptions.setOutputType(OutputType.String);
        saveOptions.setOutputFileName(new File(documentGuid).getName());

        // set password
        LoadOptions loadOptions = new LoadOptions();
        if (password != null && !password.isEmpty()) {
            loadOptions.setPassword(password);
        }

        // sign document
        SignedDocumentEntity signedDocument = new SignedDocumentEntity();
        signedDocument.setGuid(signatureHandler.sign(documentGuid, signsCollection, loadOptions, saveOptions).toString());
        return signedDocument;
    }

    /**
     * Save signature image stream
     * @return image signature
     */
    @POST
    @Path(value = "/saveImage")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public FileDescriptionEntity saveImage(SaveImageRequest saveImageRequest) {
        try {
            // get/set parameters
            String encodedImage = saveImageRequest.getImage().replace("data:image/png;base64,", "");
            FileDescriptionEntity savedImage = new FileDescriptionEntity();
            String imageName = "drawn signature.png";
            String dataImagePath = directoryUtils.getDataDirectory().getImageDirectory().getPath();
            String imagePath = String.format("%s%s%s", dataImagePath, File.separator, imageName);
            if (new File(imagePath).exists()){
                imageName =  getFreeFileName(dataImagePath, imageName).toPath().getFileName().toString();
                imagePath = String.format("%s%s%s", dataImagePath, File.separator, imageName);
            }
            byte[] decodedImg = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
            Files.write(new File(imagePath).toPath(), decodedImg);
            savedImage.setGuid(imagePath);
            // return loaded page object
            return savedImage;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Save signature stamp
     * @return stamp
     */
    @POST
    @Path(value = "/saveStamp")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public FileDescriptionEntity saveStamp(SaveStampRequest saveStampRequest){
        String previewPath = directoryUtils.getDataDirectory().getStampDirectory().getPreviewPath();
        String xmlPath = directoryUtils.getDataDirectory().getStampDirectory().getXmlPath();
        try {
            // get/set parameters
            String encodedImage = saveStampRequest.getImage().replace("data:image/png;base64,", "");
            List<StampXmlEntity> stampData = saveStampRequest.getStampData();

            FileDescriptionEntity savedImage = new FileDescriptionEntity();
            File file = getFile(previewPath, null);
            byte[] decodedImg = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
            Files.write(file.toPath(), decodedImg);
            savedImage.setGuid(file.toPath().toString());
            // stamp data to xml file saving
            StampXmlEntityList stampXmlEntityList = new StampXmlEntityList();
            stampXmlEntityList.setStampXmlEntityList(stampData);
            String xmlFileName = FilenameUtils.removeExtension(file.getName());
            String fileName = String.format("%s%s%s.xml", xmlPath, File.separator, xmlFileName);
            new XMLReaderWriter<StampXmlEntityList>().write(fileName, stampXmlEntityList);
            // return loaded page object
            return savedImage;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Save Optical signature data
     * @return optical signature
     */
    @POST
    @Path(value = "/saveOpticalCode")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public OpticalXmlEntity saveOpticalCode(SaveOpticalCodeRequest saveOpticalCodeRequest){
        BufferedImage bufImage = null;
        try {
            OpticalXmlEntity opticalCodeData = saveOpticalCodeRequest.getProperties();
            String signatureType = saveOpticalCodeRequest.getSignatureType();
            // initiate signature data wrapper with default values
            SignatureDataEntity signaturesData = new SignatureDataEntity();
            signaturesData.setImageHeight(200);
            signaturesData.setImageWidth(200);
            signaturesData.setLeft(0);
            signaturesData.setTop(0);
            // initiate signer object
            String previewPath;
            String xmlPath;
            QrCodeSigner qrSigner ;
            BarCodeSigner barCodeSigner;
            // initiate signature options collection
            SignatureOptionsCollection collection = new SignatureOptionsCollection();
            // check optical signature type
            if (signatureType.equals("qrCode")) {
                qrSigner = new QrCodeSigner(opticalCodeData, signaturesData);
                // get preview path
                QrCodeDataDirectoryEntity qrCodeDirectory = directoryUtils.getDataDirectory().getQrCodeDirectory();
                previewPath = qrCodeDirectory.getPreviewPath();
                // get xml file path
                xmlPath = qrCodeDirectory.getXmlPath();
                // generate unique file names for preview image and xml file
                collection.add(qrSigner.signImage());
            } else {
                barCodeSigner = new BarCodeSigner(opticalCodeData, signaturesData);
                // get preview path
                BarcodeDataDirectoryEntity barcodeDirectory = directoryUtils.getDataDirectory().getBarcodeDirectory();
                previewPath = barcodeDirectory.getPreviewPath();
                // get xml file path
                xmlPath = barcodeDirectory.getXmlPath();
                // generate unique file names for preview image and xml file
                collection.add(barCodeSigner.signImage());
            }
            String imageGuid = opticalCodeData.getImageGuid();
            File file = getFile(previewPath, imageGuid);
            // generate empty image for future signing with Optical signature, such approach required to get QR-Code as image
            bufImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
            // Create a graphics contents on the buffered image
            Graphics2D g2d = bufImage.createGraphics();
            // Draw graphics
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 200, 200);
            // Graphics context no longer needed so dispose it
            g2d.dispose();
            // save BufferedImage to file
            ImageIO.write(bufImage, "png", file);
            // Optical data to xml file saving
            String fileName = FilenameUtils.removeExtension(file.getName());
            new XMLReaderWriter<OpticalXmlEntity>().write(String.format("%s%s%s.xml", xmlPath, File.separator, fileName), opticalCodeData);
            // set signing save options
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(file.getName());
            saveOptions.setOverwriteExistingFiles(true);
            // set temporary signed documents path to QR-Code/BarCode image previews folder
            signatureHandler.getSignatureConfig().setOutputPath(previewPath);
            // sign generated image with Optical signature
            signatureHandler.sign(file.toPath().toString(), collection, saveOptions);
            // set signed documents path back to correct path
            signatureHandler.getSignatureConfig().setOutputPath(directoryUtils.getOutputDirectory().getPath());
            // set data for response
            opticalCodeData.setImageGuid(file.toPath().toString());
            opticalCodeData.setHeight(200);
            opticalCodeData.setWidth(200);
            // get signature preview as Base64 String
            byte[] bytes = signatureHandler.getPageImage(file.toPath().toString(), 1, "", null, 100);
            // encode ByteArray into String
            String encodedImage = new String(Base64.getEncoder().encode(bytes));
            opticalCodeData.setEncodedImage(encodedImage);
            // return loaded page object
            return opticalCodeData;
        } catch (Exception ex) {
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        } finally {
            if(bufImage != null){
                bufImage.flush();
            }
        }
    }

    /**
     * Create file in previewPath and name imageGuid
     * if the file is already exist, create new file with next number in name
     * examples, 001, 002, 003, etc
     *
     * @param previewPath path to file folder
     * @param imageGuid file name
     * @return created file
     */
    private File getFile(String previewPath, String imageGuid) {
        File folder = new File(previewPath);
        File[] listOfFiles = folder.listFiles();
        if (StringUtils.isNotEmpty(imageGuid)){
            return new File(imageGuid);
        } else {
            String fileName = "";
            for (int i = 0; i <= listOfFiles.length; i++) {
                int number = i + 1;
                // set file name, for example 001
                fileName = String.format("%03d", number);
                File file = new File(String.format("%s%s%s.png", previewPath, File.separator, fileName));
                // check if file with such name already exists
                if (file.exists()) {
                    continue;
                } else {
                    return file;
                }
            }
            return new File(String.format("%s%s001.png", previewPath, File.separator));
        }
    }

    /**
     * Save signature text
     * @return text signature
     */
    @POST
    @Path(value = "/saveText")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public TextXmlEntity saveText(SaveTextRequest saveTextRequest){
        String previewPath = directoryUtils.getDataDirectory().getTextDirectory().getPreviewPath();
        String xmlPath = directoryUtils.getDataDirectory().getTextDirectory().getXmlPath();
        BufferedImage bufImage = null;
        try {
            TextXmlEntity textData = saveTextRequest.getProperties();
            // initiate signature data wrapper with default values
            SignatureDataEntity signaturesData = new SignatureDataEntity();
            signaturesData.setImageHeight(textData.getHeight());
            signaturesData.setImageWidth(textData.getWidth());
            signaturesData.setLeft(0);
            signaturesData.setTop(0);
            // initiate signer object
            TextSigner textSigner = new TextSigner(textData, signaturesData);
            // initiate signature options collection
            SignatureOptionsCollection collection = new SignatureOptionsCollection();
            // generate unique file names for preview image and xml file
            collection.add(textSigner.signImage());
            String imageGuid = textData.getImageGuid();
            File file = getFile(previewPath, imageGuid);
            // generate empty image for future signing with Text, such approach required to get Text as image
            bufImage = new BufferedImage(signaturesData.getImageWidth(), signaturesData.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
            // Create a graphics contents on the buffered image
            Graphics2D g2d = bufImage.createGraphics();
            // Draw graphics
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, signaturesData.getImageWidth(), signaturesData.getImageHeight());
            // Graphics context no longer needed so dispose it
            g2d.dispose();
            // save BufferedImage to file
            ImageIO.write(bufImage, "png", file);
            // Save text data to an xml file
            String fileName = FilenameUtils.removeExtension(file.getName());
            new XMLReaderWriter<TextXmlEntity>().write(String.format("%s%s%s.xml", xmlPath, File.separator, fileName), textData);
            // set signing save options
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(file.getName());
            saveOptions.setOverwriteExistingFiles(true);
            // set temporary signed documents path to Text/BarCode image previews folder
            signatureHandler.getSignatureConfig().setOutputPath(previewPath);
            // sign generated image with Text
            signatureHandler.sign(file.toPath().toString(), collection, saveOptions);
            // set signed documents path back to correct path
            signatureHandler.getSignatureConfig().setOutputPath(directoryUtils.getOutputDirectory().getPath());
            // set Text data for response
            textData.setImageGuid(file.toPath().toString());
            // get Text preview as Base64 String
            byte[] bytes = signatureHandler.getPageImage(file.toPath().toString(), 1, "", null, 100);
            // encode ByteArray into String
            String encodedImage = new String(Base64.getEncoder().encode(bytes));
            textData.setEncodedImage(encodedImage);
            // return loaded page object
            return textData;
        }catch (Exception ex){
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        } finally {
            if(bufImage != null){
                bufImage.flush();
            }
        }
    }

    /**
     * Deprecated, use XMLReaderWriter
     * Stayed here for compatibility
     *
     * @return signature data
     */
    @Deprecated
    private Object loadXmlData(String fileName){
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try{
            fileInputStream = new FileInputStream(fileName);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            XMLDecoder decoder = new XMLDecoder(bufferedInputStream);
            return decoder.readObject();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            return null;
        }finally {
            try {
                bufferedInputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}