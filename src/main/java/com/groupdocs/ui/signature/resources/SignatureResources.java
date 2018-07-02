package com.groupdocs.ui.signature.resources;

import com.groupdocs.signature.config.SignatureConfig;
import com.groupdocs.signature.domain.DocumentDescription;
import com.groupdocs.signature.handler.SignatureHandler;
import com.groupdocs.signature.licensing.License;
import com.groupdocs.signature.options.OutputType;
import com.groupdocs.signature.options.SignatureOptionsCollection;
import com.groupdocs.signature.options.loadoptions.LoadOptions;
import com.groupdocs.signature.options.saveoptions.SaveOptions;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.domain.web.MediaType;
import com.groupdocs.ui.common.domain.wrapper.ExceptionWrapper;
import com.groupdocs.ui.common.domain.wrapper.FileDescriptionWrapper;
import com.groupdocs.ui.common.domain.wrapper.LoadedPageWrapper;
import com.groupdocs.ui.common.resources.Resources;
import com.groupdocs.ui.signature.signatureloader.SignatureLoader;
import com.groupdocs.ui.signature.signer.*;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;
import com.groupdocs.ui.signature.domain.wrapper.DocumentDescriptionWrapper;
import com.groupdocs.ui.signature.domain.wrapper.SignatureDataWrapper;
import com.groupdocs.ui.signature.domain.wrapper.SignatureFileDescriptionWrapper;
import com.groupdocs.ui.signature.domain.wrapper.SignedDocumentWrapper;
import com.groupdocs.ui.signature.entity.xml.OpticalXmlEntity;
import com.groupdocs.ui.signature.entity.xml.StampXmlEntity;
import com.groupdocs.ui.signature.entity.xml.TextXmlEntity;
import com.groupdocs.ui.signature.util.DirectoryUtils;
import com.groupdocs.ui.signature.views.Signature;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jetty.server.Request;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
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
    private DirectoryUtils directoryUtils;
    private String[] supportedImageFormats = {"bmp", "jpeg", "jpg", "tiff", "tif", "png"};
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
            if(relDirPath == null || relDirPath.isEmpty()){
                relDirPath = rootDirectory;
            } else {
                relDirPath = rootDirectory + "/" + relDirPath;//TODO String.Format
            }
            SignatureLoader signatureLoader = new SignatureLoader(relDirPath, globalConfiguration);
            ArrayList<SignatureFileDescriptionWrapper> fileList;
            switch (signatureType) {
                case "digital":  fileList = signatureLoader.LoadFiles();
                    break;
                case "image": fileList = signatureLoader.LoadImageSignatures();
                    break;
                case "stamp": fileList = signatureLoader.LoadStampSignatures(DataDirectoryEntity.DATA_PREVIEW_FOLDER, DataDirectoryEntity.DATA_XML_FOLDER);
                    break;
                case "text": fileList = signatureLoader.LoadStampSignatures(DataDirectoryEntity.DATA_PREVIEW_FOLDER, DataDirectoryEntity.DATA_XML_FOLDER);
                    break;
                default:  fileList = signatureLoader.LoadFiles();
                    break;
            }
            return objectToJson(fileList);
        }catch (Exception ex){
            return generateException(ex);
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
            ArrayList<DocumentDescriptionWrapper> pagesDescription = new ArrayList<>();
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
            return generateException(ex, password);
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
            String encodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(encodedImage);
            // return loaded page object
            return objectToJson(loadedPage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
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
        int count;
        byte[] buff = new byte[16 * 1024];
        OutputStream out = response.getOutputStream();
        // set response content type
        setResponseContentType(response, MediaType.APPLICATION_OCTET_STREAM);
        // get document path
        String documentGuid = request.getParameter("path");
        Boolean signed = Boolean.valueOf(request.getParameter("signed"));
        String fileName = new File(documentGuid).getName();
        // set response content disposition
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        BufferedOutputStream outStream = null;
        BufferedInputStream inputStream = null;
        String pathToDownload;
        if(signed) {
            pathToDownload = String.format("%s/%s", directoryUtils.getOutputDirectory().getPath(), fileName);
        } else {
            pathToDownload = String.format("%s/%s", directoryUtils.getFilesDirectory().getPath(), fileName);
        }
        try {
            // download the document
            inputStream = new BufferedInputStream(new FileInputStream(pathToDownload));
            outStream = new BufferedOutputStream(out);
            while ((count = inputStream.read(buff)) != -1) {
                outStream.write(buff, 0, count);
            }
            return outStream;
        } catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
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
            InputStream uploadedInputStream;
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
            String documentStoragePath;
            if(signatureType == null || signatureType.isEmpty()){
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
            // save the file
            File file = new File(documentStoragePath + "/" + fileName); //TODO String.Format
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
            uploadedDocument.setGuid(documentStoragePath + "/" + fileName); //TODO String.Format
            if(signatureType.equals("image")){
                // get page image
                byte[] bytes = Files.readAllBytes(new File(uploadedDocument.getGuid()).toPath());
                // encode ByteArray into String
                String encodedImage = new String(Base64.getEncoder().encode(bytes));
                uploadedDocument.setImage(encodedImage);
            }
            return objectToJson(uploadedDocument);
        }catch(Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
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
            int pageNumber = getJsonInteger(requestBody, "page"); //TODO not used
            String password = getJsonString(requestBody, "password"); // TODO not used
            LoadedPageWrapper loadedPage = new LoadedPageWrapper();
            // get page image
            byte[] bytes = Files.readAllBytes( new File(documentGuid).toPath());
            // encode ByteArray into String
            String encodedImage = new String(Base64.getEncoder().encode(bytes));
            loadedPage.setPageImage(encodedImage);
            // return loaded page object
            return objectToJson(loadedPage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
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
            // get signed document name
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
            // prepare signing options and sign document
            switch (signaturesData[0].getDocumentType()){
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
                    signedDocument.setGuid(signatureHandler.sign(documentGuid, signer.signCell(), loadOptions, saveOptions).toString());
                    break;
            }
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex, password);
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
                    signaturesData[i].setDocumentType("image");
                }
                // initiate image signer object
                ImageSigner signer = new ImageSigner(signaturesData[i]);
                // prepare signing options and sign document
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
            signedDocument.setGuid(signatureHandler.sign(documentGuid, signsCollection, loadOptions, saveOptions).toString());
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex, password);
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
        String xmlPath = directoryUtils.getDataDirectory().getStampDirectory().getXmlPath();
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
            // mimeType should now be something like "image/png" if the document is image
            if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                signaturesData[0].setDocumentType("image");
            }

            for(int i = 0; i < signaturesData.length; i++) {
                String xmlFileName = FilenameUtils.removeExtension(new File(signaturesData[i].getSignatureGuid()).getName());
                // Load xml data
                StampXmlEntity[] stampData = (StampXmlEntity[]) loadXmlData(xmlPath, xmlFileName);
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
            signedDocument.setGuid(signatureHandler.sign(documentGuid, signsCollection, loadOptions, saveOptions).toString());
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex, password);
        }
    }

    /**
     * Sign document with Optical signature
     * @param request
     * @param response
     * @return signed document info
     */
    @POST
    @Path(value = "/signOptical")
    public Object signOptical(@Context HttpServletRequest request, @Context HttpServletResponse response){
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
            String signatureType = signaturesData[0].getSignatureType();
            // set signed document save options
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(new File(documentGuid).getName());
            // set document password if required
            LoadOptions loadOptions = new LoadOptions();
            if (password != null && !password.isEmpty()) {
                loadOptions.setPassword(password);
            }
            // initiate signed document wrapper
            SignedDocumentWrapper signedDocument = new SignedDocumentWrapper();
            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // get xml files root path
            String xmlPath;
            if(signatureType.equals("qrCode")) {
                xmlPath = directoryUtils.getDataDirectory().getQrCodeDirectory().getXmlPath();
            } else {
                xmlPath = directoryUtils.getDataDirectory().getBarcodeDirectory().getXmlPath();
            }
            // prepare signing options and sign document
            for(int i = 0; i < signaturesData.length; i++) {
                // get xml data of the QR-Code
                String xmlFileName = FilenameUtils.removeExtension(new File(signaturesData[i].getSignatureGuid()).getName());
                // Load xml data
                OpticalXmlEntity opticalCodeData = (OpticalXmlEntity) loadXmlData(xmlPath, xmlFileName);
                // check if document type is image
                if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                    signaturesData[i].setDocumentType("image");
                }
                // initiate QRCode signer object
                QrCodeSigner qrSigner;
                BarCodeSigner barSigner;
                if(signatureType.equals("qrCode")) {
                     qrSigner = new QrCodeSigner(opticalCodeData, signaturesData[i]);
                    // prepare signing options and sign document
                    switch (signaturesData[i].getDocumentType()) {
                        case "Portable Document Format":
                            signsCollection.add(qrSigner.signPdf());
                            break;
                        case "Microsoft Word":
                            signsCollection.add(qrSigner.signWord());
                            break;
                        case "Microsoft PowerPoint":
                            signsCollection.add(qrSigner.signSlides());
                            break;
                        case "image":
                            signsCollection.add(qrSigner.signImage());
                            break;
                        case "Microsoft Excel":
                            signsCollection.add(qrSigner.signCells());
                            break;
                    }
                } else {
                    barSigner = new BarCodeSigner(opticalCodeData, signaturesData[i]);
                    // prepare signing options and sign document
                    switch (signaturesData[i].getDocumentType()) {
                        case "Portable Document Format":
                            signsCollection.add(barSigner.signPdf());
                            break;
                        case "Microsoft Word":
                            signsCollection.add(barSigner.signWord());
                            break;
                        case "Microsoft PowerPoint":
                            signsCollection.add(barSigner.signSlides());
                            break;
                        case "image":
                            signsCollection.add(barSigner.signImage());
                            break;
                        case "Microsoft Excel":
                            signsCollection.add(barSigner.signCells());
                            break;
                    }
                }
            }
            // sign the document
            signedDocument.setGuid(signatureHandler.sign(documentGuid, signsCollection, loadOptions, saveOptions).toString());
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex, password);
        }
    }

    /**
     * Sign document with Text signature
     * @param request
     * @param response
     * @return signed document info
     */
    @POST
    @Path(value = "/signText")
    public Object signText(@Context HttpServletRequest request, @Context HttpServletResponse response){
        String password = "";
        String xmlPath = directoryUtils.getDataDirectory().getTextDirectory().getXmlPath();
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String documentGuid = getJsonString(requestBody, "guid");
            password = getJsonString(requestBody, "password");
            SignatureDataWrapper[] signaturesData = (SignatureDataWrapper[]) getJsonObject(requestBody, "signaturesData", SignatureDataWrapper[].class);
            // set signed document save options
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(new File(documentGuid).getName());
            // set document password if required
            LoadOptions loadOptions = new LoadOptions();
            if (password != null && !password.isEmpty()) {
                loadOptions.setPassword(password);
            }
            // initiate signed document wrapper
            SignedDocumentWrapper signedDocument = new SignedDocumentWrapper();
            SignatureOptionsCollection signsCollection = new SignatureOptionsCollection();
            // prepare signing options and sign document
            for(int i = 0; i < signaturesData.length; i++) {
                // get xml data of the Text signature
                String xmlFileName = FilenameUtils.removeExtension(new File(signaturesData[i].getSignatureGuid()).getName());
                // Load xml data
                TextXmlEntity textData = (TextXmlEntity) loadXmlData(xmlPath, xmlFileName);
                // check if document type is image
                if (Arrays.asList(supportedImageFormats).contains(FilenameUtils.getExtension(documentGuid))) {
                    signaturesData[i].setDocumentType("image");
                }
                // initiate QRCode signer object
                TextSigner textSigner = new TextSigner(textData, signaturesData[i]);
                // prepare signing options and sign document
                switch (signaturesData[i].getDocumentType()) {
                    case "Portable Document Format":
                        signsCollection.add(textSigner.signPdf());
                        break;
                    case "Microsoft Word":
                        signsCollection.add(textSigner.signWord());
                        break;
                    case "Microsoft PowerPoint":
                        signsCollection.add(textSigner.signSlides());
                        break;
                    case "image":
                        signsCollection.add(textSigner.signImage());
                        break;
                    case "Microsoft Excel":
                        signsCollection.add(textSigner.signCells());
                        break;
                }
            }
            // sign the document
            signedDocument.setGuid(signatureHandler.sign(documentGuid, signsCollection, loadOptions, saveOptions).toString());
            // return loaded page object
            return objectToJson(signedDocument);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex, password);
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
            String imagePath = String.format("%s/%s", directoryUtils.getDataDirectory().getImageDirectory().getPath(), imageName);
            if (new File(imagePath).exists()){
                imageName =  getFreeFileName(directoryUtils.getDataDirectory().getImageDirectory().getPath(), imageName).toPath().getFileName().toString();
            }
            byte[] decodedImg = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
            Files.write(new File(imagePath).toPath(), decodedImg);
            savedImage.setGuid(imagePath);
            // return loaded page object
            return objectToJson(savedImage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
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
        String previewPath = directoryUtils.getDataDirectory().getStampDirectory().getPreviewPath();
        String xmlPath = directoryUtils.getDataDirectory().getStampDirectory().getXmlPath();
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            // get/set parameters
            String encodedImage = getJsonString(requestBody, "image").replace("data:image/png;base64,", "");
            StampXmlEntity[] stampData = (StampXmlEntity[]) getJsonObject(requestBody, "stampData", StampXmlEntity[].class);

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
            saveXmlData(xmlPath, newFileName, stampData);
            // return loaded page object
            return objectToJson(savedImage);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
        }
    }

    /**
     * Save Optical signature data
     * @param request
     * @param response
     * @return stamp
     */
    @POST
    @Path(value = "/saveOpticalCode")
    public Object saveOpticalCode(@Context HttpServletRequest request, @Context HttpServletResponse response){
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            OpticalXmlEntity opticalCodeData = (OpticalXmlEntity) getJsonObject(requestBody, "properties", OpticalXmlEntity.class);
            String signatureType = getJsonString(requestBody, "signatureType");
            // initiate signature data wrapper with default values
            SignatureDataWrapper signaturesData = new SignatureDataWrapper();
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
            if(signatureType.equals("qrCode")) {
                qrSigner = new QrCodeSigner(opticalCodeData, signaturesData);
                // get preview path
                previewPath = directoryUtils.getDataDirectory().getQrCodeDirectory().getPreviewPath();
                // get xml file path
                xmlPath = directoryUtils.getDataDirectory().getQrCodeDirectory().getXmlPath();
                // generate unique file names for preview image and xml file
                collection.add(qrSigner.signImage());
            } else {
                barCodeSigner = new BarCodeSigner(opticalCodeData, signaturesData);
                // get preview path
                previewPath = directoryUtils.getDataDirectory().getBarcodeDirectory().getPreviewPath();
                // get xml file path
                xmlPath = directoryUtils.getDataDirectory().getBarcodeDirectory().getXmlPath();
                // generate unique file names for preview image and xml file
                collection.add(barCodeSigner.signImage());
            }
            File file = null;
            File folder = new File(previewPath);
            File[] listOfFiles = folder.listFiles();
            String fileName = "";
            if(opticalCodeData.getImageGuid() != null && !opticalCodeData.getImageGuid().isEmpty()){
                file = new File(opticalCodeData.getImageGuid());
                fileName = FilenameUtils.getBaseName(opticalCodeData.getImageGuid());
            } else {
                for (int i = 0; i <= listOfFiles.length; i++) {
                    int number = i + 1;
                    // set file name, for example 001
                    fileName = String.format("%03d", number);
                    file = new File(previewPath + "/" + fileName + ".png");
                    // check if file with such name already exists
                    if (file.exists()) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
            // generate empty image for future signing with Optical signature, such approach required to get QR-Code as image
            BufferedImage bufImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
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
            saveXmlData(xmlPath, fileName, opticalCodeData);
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
            String incodedImage = new String(Base64.getEncoder().encode(bytes));
            opticalCodeData.setEncodedImage(incodedImage);
            // return loaded page object
            return objectToJson(opticalCodeData);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
        }
    }

    /**
     * Save signature text
     * @param request
     * @param response
     * @return stamp
     */
    @POST
    @Path(value = "/saveText")
    public Object saveText(@Context HttpServletRequest request, @Context HttpServletResponse response){
        String previewPath = directoryUtils.getDataDirectory().getTextDirectory().getPreviewPath();
        String xmlPath = directoryUtils.getDataDirectory().getTextDirectory().getXmlPath();
        try {
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            // get request body
            String requestBody = getRequestBody(request);
            TextXmlEntity textData = (TextXmlEntity) getJsonObject(requestBody, "properties", TextXmlEntity.class);
            // initiate signature data wrapper with default values
            SignatureDataWrapper signaturesData = new SignatureDataWrapper();
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
            File file = null;
            File folder = new File(previewPath);
            File[] listOfFiles = folder.listFiles();
            String fileName = "";
            if(textData.getImageGuid() != null && !textData.getImageGuid().isEmpty()){
                file = new File(textData.getImageGuid());
                fileName = FilenameUtils.getBaseName(textData.getImageGuid());
            } else {
                for (int i = 0; i <= listOfFiles.length; i++) {
                    int number = i + 1;
                    // set file name, for example 001
                    fileName = String.format("%03d", number);
                    file = new File(previewPath + "/" + fileName + ".png");
                    // check if file with such name already exists
                    if (file.exists()) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
            // generate empty image for future signing with Text, such approach required to get Text as image
            BufferedImage bufImage = new BufferedImage(signaturesData.getImageWidth(), signaturesData.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
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
            saveXmlData(xmlPath, fileName, textData);
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
            return objectToJson(textData);
        }catch (Exception ex){
            // set response content type
            setResponseContentType(response, MediaType.APPLICATION_JSON);
            return generateException(ex);
        }
    }

    /**
     *
     * @param ex
     * @return
     */
    private Object generateException(Exception ex){
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
        exceptionWrapper.setMessage(ex.getMessage());
        exceptionWrapper.setException(ex);
        return objectToJson(exceptionWrapper);
    }

    /**
     *
     * @param ex
     * @param password
     * @return
     */
    private Object generateException(Exception ex, String password){
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

    /**
     *
     * @param xmlPath
     * @param xmlFileName
     * @return
     */
    private Object loadXmlData(String xmlPath, String xmlFileName){
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try{
            fileInputStream = new FileInputStream(String.format("%s/%s.xml", xmlPath ,xmlFileName));
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

    /**
     *
     * @param xmlPath
     * @param xmlFileName
     * @param xmlEntity
     */
    private void saveXmlData(String xmlPath, String xmlFileName, Object xmlEntity){
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        XMLEncoder encoder = null;
        try{
            fileOutputStream = new FileOutputStream(String.format("%s/%s.xml", xmlPath, xmlFileName));
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            encoder = new XMLEncoder(bufferedOutputStream);
            encoder.writeObject(xmlEntity);
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }finally {
            try {
                encoder.close();
                bufferedOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}