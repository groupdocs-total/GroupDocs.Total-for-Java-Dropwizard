package com.groupdocs.ui.signature.resources;

import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.domain.wrapper.ExceptionWrapper;
import com.groupdocs.ui.common.domain.wrapper.FileDescriptionWrapper;
import com.groupdocs.ui.common.domain.wrapper.LoadedPageWrapper;
import com.groupdocs.ui.common.resources.Resources;
import com.groupdocs.ui.common.domain.web.MediaType;
import com.groupdocs.ui.signature.SignaturesLoader.SignatureLoader;
import com.groupdocs.ui.signature.Signer.DigitalSigner;
import com.groupdocs.ui.signature.Signer.ImageSigner;
import com.groupdocs.ui.signature.Signer.StampSigner;
import com.groupdocs.ui.signature.domain.wrapper.StampDataWrapper;
import com.groupdocs.ui.signature.domain.wrapper.SignatureFileDescriptionWrapper;
import com.groupdocs.ui.signature.domain.wrapper.SignatureDataWrapper;
import com.groupdocs.ui.signature.domain.wrapper.SignedDocumentWrapper;
import com.groupdocs.ui.signature.domain.wrapper.DocumentDescriptionWrapper;
import com.groupdocs.ui.signature.views.Signature;
import com.groupdocs.signature.domain.DocumentDescription;
import com.groupdocs.signature.handler.SignatureHandler;
import com.groupdocs.signature.licensing.License;
import com.groupdocs.signature.options.OutputType;
import com.groupdocs.signature.options.SignatureOptionsCollection;
import com.groupdocs.signature.options.loadoptions.LoadOptions;
import com.groupdocs.signature.options.saveoptions.SaveOptions;
import com.groupdocs.signature.config.SignatureConfig;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jetty.server.Request;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

/**
 * Signature
 *
 * @author Aspose Pty Ltd
 */

@Path(value = "/signature")
public class SignatureResources extends Resources {
    private final SignatureHandler signatureHandler;
    private final String signaturesRootFolder = "/SignatureData";
    private final String stampsRootFolder = "/Stamps";
    private final String stampsPreviewFolder = "/Preview";
    private final String stampsXmlFolder = "/XML";
    private final String certificatesFolder = "/Digital";
    private final String imagesFolder = "/Image";
    private String[] supportedImageFormats = {"bmp", "jpeg", "jpg", "tiff", "tif", "png"};
    /**
     * Constructor
     * @param globalConfiguration global configuration object
     * @throws UnknownHostException
     */
    public SignatureResources(GlobalConfiguration globalConfiguration) throws UnknownHostException {
        super(globalConfiguration);

        // create total application configuration
        SignatureConfig config = new SignatureConfig();
        if(!new File(globalConfiguration.getSignature().getFilesDirectory()).isAbsolute()) {
            globalConfiguration.getSignature().setFilesDirectory(new File("").getAbsolutePath() + globalConfiguration.getSignature().getFilesDirectory());
        }
        String signatureDataPath = "";
        if(globalConfiguration.getSignature().getDataDirectory() == null || globalConfiguration.getSignature().getDataDirectory().isEmpty()){
            signatureDataPath = globalConfiguration.getSignature().getFilesDirectory() + signaturesRootFolder;
        } else {
            signatureDataPath = globalConfiguration.getSignature().getFilesDirectory() + globalConfiguration.getSignature().getDataDirectory();
        }
        globalConfiguration.getSignature().setDataDirectory(signatureDataPath);
        if(!Files.exists( new File(signatureDataPath).toPath())) {
            new File(signatureDataPath).mkdir();
        }
        if(!Files.exists(new File(signatureDataPath + certificatesFolder).toPath())){
            new File(signatureDataPath + certificatesFolder).mkdir();
        }
        if(!Files.exists(new File(signatureDataPath + imagesFolder).toPath())){
            new File(signatureDataPath + imagesFolder).mkdir();
        }
        if(!Files.exists(new File(signatureDataPath + stampsRootFolder).toPath())){
            new File(signatureDataPath + stampsRootFolder).mkdir();
            new File(signatureDataPath + stampsRootFolder + stampsPreviewFolder).mkdir();
            new File(signatureDataPath + stampsRootFolder + stampsXmlFolder).mkdir();
        }
        if(globalConfiguration.getSignature().getOutputDirectory() == null || globalConfiguration.getSignature().getOutputDirectory().isEmpty()){
            globalConfiguration.getSignature().setOutputDirectory(globalConfiguration.getSignature().getFilesDirectory());
        } else {
            globalConfiguration.getSignature().setOutputDirectory(globalConfiguration.getSignature().getFilesDirectory() + globalConfiguration.getSignature().getOutputDirectory());
        }
        if(!Files.exists(new File(globalConfiguration.getSignature().getOutputDirectory()).toPath())){
            new File(globalConfiguration.getSignature().getOutputDirectory()).mkdir();
        }
        config.setStoragePath(globalConfiguration.getSignature().getFilesDirectory());
        config.setCertificatesPath(signatureDataPath + certificatesFolder);
        config.setImagesPath(signatureDataPath + imagesFolder);
        config.setOutputPath(globalConfiguration.getSignature().getOutputDirectory());
        // set GroupDocs license
        License license = new License();
        license.setLicense(globalConfiguration.getApplication().getLicensePath());
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
        String signatureType = "";
        if(requestBody.contains("signatureType")){
            signatureType = getJsonString(requestBody, "signatureType");
        }
        // get file list from storage path
        try{
            String rootDirectory;
            switch (signatureType) {
                case "digital":  rootDirectory = signatureHandler.getSignatureConfig().getCertificatesPath();
                    break;
                case "image": rootDirectory = signatureHandler.getSignatureConfig().getImagesPath();
                    break;
                case "stamp": rootDirectory = globalConfiguration.getSignature().getDataDirectory() + stampsRootFolder;
                    break;
                default:  rootDirectory = signatureHandler.getSignatureConfig().getStoragePath();
                    break;
            }
            // get all the files from a directory
            if(relDirPath == null || relDirPath.isEmpty()){
                relDirPath = rootDirectory;
            } else {
                relDirPath = rootDirectory + "/" + relDirPath;
            }
            SignatureLoader signatureLoader = new SignatureLoader(relDirPath, globalConfiguration);
            ArrayList<SignatureFileDescriptionWrapper> fileList = new ArrayList<SignatureFileDescriptionWrapper>();
            switch (signatureType) {
                case "digital":  fileList = signatureLoader.LoadFiles();
                    break;
                case "image": fileList = signatureLoader.LoadImageSignatures();
                    break;
                case "stamp": fileList = signatureLoader.LoadStampSignatures(stampsPreviewFolder, stampsXmlFolder);
                    break;
                default:  fileList = signatureLoader.LoadFiles();
                    break;
            }
            return objectToJson(fileList);
        }catch (Exception ex){
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setMessage(ex.getMessage());
            exceptionWrapper.setException(ex);
            return objectToJson(exceptionWrapper);
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
            DocumentDescription documentDescription;
            // get document info container
            documentDescription = signatureHandler.getDocumentDescription(documentGuid, password);
            ArrayList<DocumentDescriptionWrapper> pagesDescription = new ArrayList<DocumentDescriptionWrapper>();
            // get info about each document page
            for(int i = 1; i <= documentDescription.getPageCount(); i++) {
                //initiate custom Document description object
                DocumentDescriptionWrapper description = new DocumentDescriptionWrapper();
                // get current page size
                java.awt.Dimension pageSize = signatureHandler.getDocumentPageSize(documentGuid, i, password, (double)0, (double)0, null);
                // set current page info for result
                description.setHeight(pageSize.getHeight());
                description.setWidth(pageSize.getWidth());
                description.setNumber(i);
                pagesDescription.add(description);
            }
            // return document description
            return objectToJson(pagesDescription);
        }catch (Exception ex){
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            // set exception message
            if(ex.getMessage().contains("password") && password.isEmpty()) {
                exceptionWrapper.setMessage("Password Required");
            }else if(ex.getMessage().contains("password") && !password.isEmpty()){
                exceptionWrapper.setMessage("Incorrect password");
            }else{
                exceptionWrapper.setMessage(ex.getMessage());
                exceptionWrapper.setException(ex);
            }
            return objectToJson(exceptionWrapper);
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
            LoadedPageWrapper loadedPage = new LoadedPageWrapper();
            // get page image
            byte[] bytes = signatureHandler.getPageImage(documentGuid, pageNumber, password, null, 100);
            // encode ByteArray into String
            String incodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(incodedImage);
            // return loaded page object
            return objectToJson(loadedPage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setMessage(ex.getMessage());
            exceptionWrapper.setException(ex);
            return objectToJson(exceptionWrapper);
        }
    }

    /**
     * Download document
     * @param request
     * @param response
     */
    @GET
    @Path(value = "/downloadDocument")
    public Object downloadDocument(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        int bytesRead = 0;
        int count = 0;
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
        try {
            // download the document
            inputStream = new BufferedInputStream(new FileInputStream(documentGuid));
            outStream = new BufferedOutputStream(out);
            while ((count = inputStream.read(buff)) != -1) {
                outStream.write(buff, 0, count);
            }
            return outStream;
        } catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setMessage(ex.getMessage());
            exceptionWrapper.setException(ex);
            return objectToJson(exceptionWrapper);
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
            // get signature type
            String signatureType = request.getParameter("signatureType");
            // get rewrite mode
            boolean rewrite = Boolean.parseBoolean(request.getParameter("rewrite"));
            InputStream uploadedInputStream = null;
            String fileName = "";
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
            String documentStoragePath;
            if(signatureType == null || signatureType.isEmpty()){
                signatureType = "";
            }
            switch(signatureType){
                case "digital": documentStoragePath = signatureHandler.getSignatureConfig().getCertificatesPath();
                    break;
                case "image": documentStoragePath = signatureHandler.getSignatureConfig().getImagesPath();
                    break;
                default:  documentStoragePath = signatureHandler.getSignatureConfig().getStoragePath();
                    break;
            }
            // save the file
            File file = new File(documentStoragePath + "/" + fileName);
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
            SignatureFileDescriptionWrapper uploadedDocument = new SignatureFileDescriptionWrapper();
            uploadedDocument.setGuid(documentStoragePath + "/" + fileName);
            if(signatureType.equals("image")){
                // get page image
                byte[] bytes = Files.readAllBytes(new File(uploadedDocument.getGuid()).toPath());
                // encode ByteArray into String
                String incodedImage = new String(Base64.getEncoder().encode(bytes));
                uploadedDocument.setImage(incodedImage);
            }
            return objectToJson(uploadedDocument);
        }catch(Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setMessage(ex.getMessage());
            exceptionWrapper.setException(ex);
            return objectToJson(exceptionWrapper);
        }
    }

    /**
     * Get signature image stream - temporarlly workaround used until release of the GroupDocs.Signature 18.5, after release will be removed
     * @param request
     * @param response
     * @return document page
     */
    @POST
    @Path(value = "/loadSignatureImage")
    public Object loadSignatureImage(@Context HttpServletRequest request, @Context HttpServletResponse response){
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            int pageNumber = getJsonInteger(requestBody, "page");
            String password = getJsonString(requestBody, "password");
            LoadedPageWrapper loadedPage = new LoadedPageWrapper();
            // get page image
            byte[] bytes = Files.readAllBytes( new File(documentGuid).toPath());
            // encode ByteArray into String
            String incodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(incodedImage);
            // return loaded page object
            return objectToJson(loadedPage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setMessage(ex.getMessage());
            exceptionWrapper.setException(ex);
            return objectToJson(exceptionWrapper);
        }
    }

    /**
     * Sign document with digital signature
     * @param request
     * @param response
     * @return signed document info
     */
    @POST
    @Path(value = "/signDigital")
    public Object signDigital(@Context HttpServletRequest request, @Context HttpServletResponse response){
        String password = "";
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            password = getJsonString(requestBody, "password");
            SignatureDataWrapper[] signaturesData = (SignatureDataWrapper[]) getJsonObject(requestBody, "signaturesData", SignatureDataWrapper[].class);
            String signatureGuid = signaturesData[0].getSignatureGuid();
            // get signied document name
            String signedFileName = new File(documentGuid).getName();
            // initiate signed document wrapper
            SignedDocumentWrapper signedDocument = new SignedDocumentWrapper();
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(signedFileName);
            LoadOptions loadOptions = new LoadOptions();
            if(password != null && !password.isEmpty()) {
                loadOptions.setPassword(password);
            }
            // initiate digital signer
            DigitalSigner signer = new DigitalSigner(signaturesData[0], password);
            // prepare sgining options and sign document
            switch (signaturesData[0].getDocumentType()){
                case "Portable Document Format":
                    // sign document
                    signatureHandler.sign(documentGuid, signer.signPdf(), loadOptions, saveOptions);
                    break;
                case "Microsoft Word":
                    // sign document
                    signatureHandler.sign(documentGuid, signer.signWord(), loadOptions, saveOptions);
                    break;
                case "Microsoft Excel":
                    // sign document
                    signatureHandler.sign(documentGuid, signer.signCell(), loadOptions, saveOptions);
                    break;
            }
            signedDocument.setGuid(signatureHandler.getSignatureConfig().getOutputPath() + "/" + signedFileName);
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            if(ex.getMessage().contains("password") && password.isEmpty()) {
                exceptionWrapper.setMessage("Password Required");
            }else if(ex.getMessage().contains("password") && !password.isEmpty()){
                exceptionWrapper.setMessage("Incorrect password");
            }else{
                exceptionWrapper.setMessage(ex.getMessage());
                exceptionWrapper.setException(ex);
            }
            return objectToJson(exceptionWrapper);
        }
    }

    /**
     * Sign document with image signature
     * @param request
     * @param response
     * @return signed document info
     */
    @POST
    @Path(value = "/signImage")
    public Object signImage(@Context HttpServletRequest request, @Context HttpServletResponse response){
        String password = "";
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            password = getJsonString(requestBody, "password");
            SignatureDataWrapper[] signaturesData = (SignatureDataWrapper[]) getJsonObject(requestBody, "signaturesData", SignatureDataWrapper[].class);
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(new File(documentGuid).getName());
            LoadOptions loadOptions = new LoadOptions();
            if (password != null && !password.isEmpty()) {
                loadOptions.setPassword(password);
            }
            // initiate signed document wrapper
            SignedDocumentWrapper signedDocument = new SignedDocumentWrapper();
            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // set signature password if required
            for(int i = 0; i < signaturesData.length; i++) {
                // check if document type is image
                if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                    signaturesData[0].setDocumentType("image");
                }
                // initiate image signer object
                ImageSigner signer = new ImageSigner(signaturesData[i]);
                // prepare sgining options and sign document
                switch (signaturesData[i].getDocumentType()) {
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
                        signsCollection.add(signer.signCell());
                        break;
                }
            }
            // sign the document
            signatureHandler.sign(documentGuid, signsCollection, loadOptions, saveOptions);
            signedDocument.setGuid(signatureHandler.getSignatureConfig().getOutputPath() + "/" + new File(documentGuid).getName());
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            if(ex.getMessage().contains("password") && password.isEmpty()) {
                exceptionWrapper.setMessage("Password Required");
            }else if(ex.getMessage().contains("password") && !password.isEmpty()){
                exceptionWrapper.setMessage("Incorrect password");
            }else{
                exceptionWrapper.setMessage(ex.getMessage());
                exceptionWrapper.setException(ex);
            }
            return objectToJson(exceptionWrapper);
        }
    }

    /**
     * Sign document with stamp signature
     * @param request
     * @param response
     * @return signed document info
     */
    @POST
    @Path(value = "/signStamp")
    public Object signStamp(@Context HttpServletRequest request, @Context HttpServletResponse response){
        String password = "";
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            password = getJsonString(requestBody, "password");
            SignatureDataWrapper[] signaturesData = (SignatureDataWrapper[]) getJsonObject(requestBody, "signaturesData", SignatureDataWrapper[].class);
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(new File(documentGuid).getName());
            LoadOptions loadOptions = new LoadOptions();
            if (password != null && !password.isEmpty()) {
                loadOptions.setPassword(password);
            }
            // initiate signed document wrapper
            SignedDocumentWrapper signedDocument = new SignedDocumentWrapper();
            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // set signature password if required
            String xmlPath = globalConfiguration.getSignature().getDataDirectory() + stampsRootFolder + stampsXmlFolder;
            // mimeType should now be something like "image/png" if the document is image
            if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                signaturesData[0].setDocumentType("image");
            }
            int redColor = 0;
            int greenColor = 0;
            int blueColor = 0;
            for(int i = 0; i < signaturesData.length; i++) {
                String stampName = FilenameUtils.removeExtension(new File(signaturesData[i].getSignatureGuid()).getName());
                // prepare signing options and sign document
                XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(xmlPath + "/" + stampName +".xml")));
                StampDataWrapper[] stampData = (StampDataWrapper[])decoder.readObject();
                // since stamp ine are added stating from the most outer line we need to reverse the stamp data array
                ArrayUtils.reverse(stampData);
                // initiate stamp signer
                StampSigner signer = new StampSigner(stampData, signaturesData[i]);
                switch (signaturesData[0].getDocumentType()) {
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
                        signsCollection.add(signer.signCell());
                        break;
                }
            }
            // sign the document
            signatureHandler.sign(documentGuid, signsCollection, loadOptions, saveOptions);
            signedDocument.setGuid(signatureHandler.getSignatureConfig().getOutputPath() + "/" + new File(documentGuid).getName());
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            if(ex.getMessage().contains("password") && password.isEmpty()) {
                exceptionWrapper.setMessage("Password Required");
            }else if(ex.getMessage().contains("password") && !password.isEmpty()){
                exceptionWrapper.setMessage("Incorrect password");
            }else{
                exceptionWrapper.setMessage(ex.getMessage());
                exceptionWrapper.setException(ex);
            }
            return objectToJson(exceptionWrapper);
        }
    }

    /**
     * Save signature image stream
     * @param request
     * @param response
     * @return document page
     */
    @POST
    @Path(value = "/saveImage")
    public Object saveImage(@Context HttpServletRequest request, @Context HttpServletResponse response){
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String encodedImage = getJsonString(requestBody, "image").replace("data:image/png;base64,", "");
            FileDescriptionWrapper savedImage = new FileDescriptionWrapper();
            String imageName = "drawn signature.png";
            if (new File(signatureHandler.getSignatureConfig().getImagesPath() + "/" + imageName).exists()){
                imageName =  getFreeFileName(signatureHandler.getSignatureConfig().getImagesPath(), imageName).toPath().getFileName().toString();
            }
            String imagePath =  signatureHandler.getSignatureConfig().getImagesPath() + "/" + imageName;
            byte[] decodedImg = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
            Files.write(new File(imagePath).toPath(), decodedImg);
            savedImage.setGuid(imagePath);
            // return loaded page object
            return objectToJson(savedImage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setMessage(ex.getMessage());
            exceptionWrapper.setException(ex);
            return objectToJson(exceptionWrapper);
        }
    }

    /**
     * Save signature stamp
     * @param request
     * @param response
     * @return stamp
     */
    @POST
    @Path(value = "/saveStamp")
    public Object saveStamp(@Context HttpServletRequest request, @Context HttpServletResponse response){
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String encodedImage = getJsonString(requestBody, "image").replace("data:image/png;base64,", "");
            StampDataWrapper[] stampData = (StampDataWrapper[]) getJsonObject(requestBody, "stampData", StampDataWrapper[].class);
            String previewPath = globalConfiguration.getSignature().getDataDirectory() + stampsRootFolder + stampsPreviewFolder;
            String xmlPath = globalConfiguration.getSignature().getDataDirectory() + stampsRootFolder + stampsXmlFolder;
            String newFileName = "";
            FileDescriptionWrapper savedImage = new FileDescriptionWrapper();
            File file = null;
            File folder = new File(previewPath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i <= listOfFiles.length; i++) {
                int number = i + 1;
                newFileName = String.format("%03d", number);
                file = new File(previewPath + "/" + newFileName + ".png");
                if (file.exists()) {
                    continue;
                } else {
                    break;
                }
            }
            byte[] decodedImg = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
            file.createNewFile();
            Files.write(file.toPath(), decodedImg);
            savedImage.setGuid(file.toPath().toString());
            // stamp data to xml file saving
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(xmlPath + "/" + newFileName + ".xml")));
            encoder.writeObject(stampData);
            encoder.close();

            // return loaded page object
            return objectToJson(savedImage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // set exception message
            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setMessage(ex.getMessage());
            exceptionWrapper.setException(ex);
            return objectToJson(exceptionWrapper);
        }
    }

    private Object getJsonObject(String json, String key, Type type){
        Object value = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            Gson gson = new Gson();
            value = gson.fromJson( jsonObject.get(key).toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }
}