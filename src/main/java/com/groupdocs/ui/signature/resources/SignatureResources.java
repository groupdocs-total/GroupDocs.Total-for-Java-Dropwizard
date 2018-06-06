package com.groupdocs.ui.signature.resources;

import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.domain.wrapper.ExceptionWrapper;
import com.groupdocs.ui.common.domain.wrapper.FileDescriptionWrapper;
import com.groupdocs.ui.common.domain.wrapper.LoadedPageWrapper;
import com.groupdocs.ui.common.resources.Resources;
import com.groupdocs.ui.common.domain.web.MediaType;
import com.groupdocs.ui.signature.comparator.FileNameComparator;
import com.groupdocs.ui.signature.comparator.FileTypeComparator;
import com.groupdocs.ui.signature.domain.wrapper.*;
import com.groupdocs.ui.signature.views.Signature;
import com.groupdocs.signature.domain.DocumentDescription;
import com.groupdocs.signature.handler.SignatureHandler;
import com.groupdocs.signature.licensing.License;
import com.groupdocs.signature.options.OutputType;
import com.groupdocs.signature.options.SignatureOptionsCollection;
import com.groupdocs.signature.options.digitalsignature.CellsSignDigitalOptions;
import com.groupdocs.signature.options.digitalsignature.PdfSignDigitalOptions;
import com.groupdocs.signature.options.digitalsignature.WordsSignDigitalOptions;
import com.groupdocs.signature.options.imagesignature.CellsSignImageOptions;
import com.groupdocs.signature.options.imagesignature.ImagesSignImageOptions;
import com.groupdocs.signature.options.imagesignature.SlidesSignImageOptions;
import com.groupdocs.signature.options.imagesignature.WordsSignImageOptions;
import com.groupdocs.signature.options.imagesignature.PdfSignImageOptions;
import com.groupdocs.signature.options.loadoptions.LoadOptions;
import com.groupdocs.signature.options.saveoptions.SaveOptions;
import com.groupdocs.signature.config.SignatureConfig;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.server.Request;
import org.json.JSONException;
import org.json.JSONObject;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Signature
 *
 * @author Aspose Pty Ltd
 */

@Path(value = "/signature")
public class SignatureResources extends Resources {
    private final SignatureHandler signatureHandler;
    private String stampsPath;
    /**
     * Constructor
     * @param globalConfiguration global configuration object
     * @throws UnknownHostException
     */
    public SignatureResources(GlobalConfiguration globalConfiguration) throws UnknownHostException {
        super(globalConfiguration);

        // create total application configuration
        SignatureConfig config = new SignatureConfig();

        config.setStoragePath(globalConfiguration.getSignature().getFilesDirectory());
        config.setCertificatesPath(globalConfiguration.getSignature().getCertificatePath());
        config.setImagesPath(globalConfiguration.getSignature().getImagePath());
        config.setOutputPath(globalConfiguration.getSignature().getOutputPath());
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
                default:  rootDirectory = signatureHandler.getSignatureConfig().getStoragePath();
                    break;
            }
            // get all the files from a directory
            if(relDirPath == null || relDirPath.isEmpty()){
                relDirPath = rootDirectory;
            } else {
                relDirPath = rootDirectory + "/" + relDirPath;
            }
            File directory = new File(relDirPath);
            File[] fList = directory.listFiles();
            ArrayList<SignatureFileDescriptionWrapper> fileList = new ArrayList<SignatureFileDescriptionWrapper>();
            List<File> filesList = new ArrayList<>();
            for (File file : fList) {
                filesList.add(file);
            }
            fList = null;
            // sort list of files and folders
            Collections.sort(filesList, Ordering.from(new FileTypeComparator()).compound(new FileNameComparator()));
            for (File file : filesList) {
                // check if current file/folder is hidden
                if(file.isHidden()) {
                    // ignore current file and skip to next one
                    continue;
                } else {
                    SignatureFileDescriptionWrapper fileDescription = new SignatureFileDescriptionWrapper();
                    fileDescription.setGuid(file.getAbsolutePath());
                    fileDescription.setName(file.getName());
                    // set is directory true/false
                    fileDescription.setDirectory(file.isDirectory());
                    // set file size
                    fileDescription.setSize(file.length());
                    if(signatureType.equals("image")) {
                        // get image Base64 incoded String
                        FileInputStream fileInputStreamReader = new FileInputStream(file);
                        byte[] bytes = new byte[(int)file.length()];
                        fileInputStreamReader.read(bytes);
                        fileDescription.setImage(Base64.getEncoder().encodeToString(bytes));
                    }
                    // add object to array list
                    fileList.add(fileDescription);
                }
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
            UploadedSignatureWrapper uploadedDocument = new UploadedSignatureWrapper();
            uploadedDocument.setGuid(documentStoragePath + "/" + fileName);
            if(signatureType.equals("image")){
                // get page image
                byte[] bytes = Files.readAllBytes(new File(uploadedDocument.getGuid()).toPath());
                // encode ByteArray into String
                String incodedImage = new String(Base64.getEncoder().encode(bytes));
                uploadedDocument.setSignatureImage(incodedImage);
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
            // initiate date formater
            SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yy");
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(signedFileName);
            LoadOptions loadOptions = new LoadOptions();
            if(password != null && !password.isEmpty()) {
                loadOptions.setPassword(password);
            }
            // prepare sgining options and sign document
            switch (signaturesData[0].getDocumentType()){
               case "Portable Document Format":
                    // setup digital signature options
                    PdfSignDigitalOptions pdfSignOptions = new PdfSignDigitalOptions(signatureGuid);
                    pdfSignOptions.setReason(signaturesData[0].getReason());
                    pdfSignOptions.setContact(signaturesData[0].getContact());
                    pdfSignOptions.setLocation(signaturesData[0].getAddress());
                    pdfSignOptions.setPassword(password);
                    pdfSignOptions.setSignAllPages(true);
                    if(signaturesData[0].getDate() != null && !signaturesData[0].getDate().isEmpty()) {
                       pdfSignOptions.getSignature().setSignTime(formatter.parse(signaturesData[0].getDate()));
                    }
                    // sign document
                    signatureHandler.sign(documentGuid, pdfSignOptions, loadOptions, saveOptions);
                    break;
               case "Microsoft Word":
                    // setup digital signature options
                    WordsSignDigitalOptions wordsSignOptions = new WordsSignDigitalOptions(signatureGuid);
                    wordsSignOptions.getSignature().setComments(signaturesData[0].getSignatureComment());
                    if(signaturesData[0].getDate() != null && !signaturesData[0].getDate().isEmpty()) {
                       wordsSignOptions.getSignature().setSignTime(formatter.parse(signaturesData[0].getDate()));
                    }
                    wordsSignOptions.setPassword(password);
                    wordsSignOptions.setSignAllPages(true);
                    // sign document
                    signatureHandler.sign(documentGuid, wordsSignOptions, loadOptions, saveOptions);
                    break;
               case "Microsoft Excel":
                    CellsSignDigitalOptions cellsSignOptions = new CellsSignDigitalOptions(signatureGuid);
                    cellsSignOptions.getSignature().setComments(signaturesData[0].getSignatureComment());
                    if(signaturesData[0].getDate() != null && !signaturesData[0].getDate().isEmpty()) {
                       cellsSignOptions.getSignature().setSignTime(formatter.parse(signaturesData[0].getDate()));
                    }
                    cellsSignOptions.setPassword(password);
                    cellsSignOptions.setSignAllPages(true);
                    // sign document
                    signatureHandler.sign(documentGuid, cellsSignOptions, loadOptions, saveOptions);
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

                String signatureGuid = signaturesData[i].getSignatureGuid();
                String mimeType = new MimetypesFileTypeMap().getContentType(documentGuid);
                // mimeType should now be something like "image/png" if the document is image
                if (mimeType.substring(0, 5).equalsIgnoreCase("image")) {
                    signaturesData[i].setDocumentType("image");
                }
                // prepare sgining options and sign document
                switch (signaturesData[i].getDocumentType()) {
                    case "Portable Document Format":
                        // setup image signature options
                        PdfSignImageOptions pdfSignOptions = new PdfSignImageOptions(signatureGuid);
                        // image position
                        pdfSignOptions.setLeft(signaturesData[i].getLeft());
                        pdfSignOptions.setTop(signaturesData[i].getTop());
                        pdfSignOptions.setWidth(signaturesData[i].getImageWidth());
                        pdfSignOptions.setHeight(signaturesData[i].getImageHeight());
                        pdfSignOptions.setDocumentPageNumber(signaturesData[i].getPageNumber());
                        pdfSignOptions.setRotationAngle(signaturesData[i].getAngle());
                        signsCollection.add(pdfSignOptions);
                        break;
                    case "Microsoft Word":
                        // setup image signature options with relative path - image file stores in config.ImagesPath folder
                        WordsSignImageOptions wordsSignOptions = new WordsSignImageOptions(signatureGuid);
                        wordsSignOptions.setLeft(signaturesData[i].getLeft());
                        wordsSignOptions.setTop(signaturesData[i].getTop());
                        wordsSignOptions.setWidth(signaturesData[i].getImageWidth());
                        wordsSignOptions.setHeight(signaturesData[i].getImageHeight());
                        wordsSignOptions.setDocumentPageNumber(signaturesData[i].getPageNumber());
                        wordsSignOptions.setRotationAngle(signaturesData[i].getAngle());
                        signsCollection.add(wordsSignOptions);
                        break;
                    case "Microsoft PowerPoint":
                        // setup image signature options with relative path - image file stores in config.ImagesPath folder
                        SlidesSignImageOptions slidesSignOptions = new SlidesSignImageOptions(signatureGuid);
                        slidesSignOptions.setLeft(signaturesData[i].getLeft());
                        slidesSignOptions.setTop(signaturesData[i].getTop());
                        slidesSignOptions.setWidth(signaturesData[i].getImageWidth());
                        slidesSignOptions.setHeight(signaturesData[i].getImageHeight());
                        slidesSignOptions.setDocumentPageNumber(signaturesData[i].getPageNumber());
                        slidesSignOptions.setRotationAngle(signaturesData[i].getAngle());
                        signsCollection.add(slidesSignOptions);
                        break;
                    case "image":
                        // setup image signature options with relative path - image file stores in config.ImagesPath folder
                        ImagesSignImageOptions imageSignOptions = new ImagesSignImageOptions(signatureGuid);
                        imageSignOptions.setLeft(signaturesData[i].getLeft());
                        imageSignOptions.setTop(signaturesData[i].getTop());
                        imageSignOptions.setWidth(signaturesData[i].getImageWidth());
                        imageSignOptions.setHeight(signaturesData[i].getImageHeight());
                        imageSignOptions.setDocumentPageNumber(signaturesData[i].getPageNumber());
                        imageSignOptions.setRotationAngle(signaturesData[i].getAngle());
                        signsCollection.add(imageSignOptions);
                        break;
                    case "Microsoft Excel":
                        // setup image signature options
                        CellsSignImageOptions cellsSignOptions = new CellsSignImageOptions(signatureGuid);
                        // image position
                        cellsSignOptions.setTop(signaturesData[i].getTop());
                        cellsSignOptions.setLeft(signaturesData[i].getLeft());
                        cellsSignOptions.setDocumentPageNumber(signaturesData[i].getPageNumber());
                        cellsSignOptions.setHeight(signaturesData[i].getImageHeight());
                        cellsSignOptions.setWidth(signaturesData[i].getImageWidth());
                        cellsSignOptions.setRotationAngle(signaturesData[i].getAngle());
                        signsCollection.add(cellsSignOptions);
                        break;
                }
            }
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
     * Get signature image stream - temporarlly workaround used until release of the GroupDocs.Signature 18.5, after release will be removed
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
