package com.groupdocs.ui.comparison.resources;

import com.groupdocs.comparison.common.license.License;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.LoadedPageEntity;
import com.groupdocs.ui.common.entity.web.UploadedDocumentEntity;
import com.groupdocs.ui.common.entity.web.request.FileTreeRequest;
import com.groupdocs.ui.common.exception.TotalGroupDocsException;
import com.groupdocs.ui.common.resources.Resources;
import com.groupdocs.ui.comparison.model.request.CompareFileDataRequest;
import com.groupdocs.ui.comparison.model.request.CompareRequest;
import com.groupdocs.ui.comparison.model.request.LoadResultPageRequest;
import com.groupdocs.ui.comparison.model.response.CompareResultResponse;
import com.groupdocs.ui.comparison.service.ComparisonService;
import com.groupdocs.ui.comparison.service.ComparisonServiceImpl;
import com.groupdocs.ui.comparison.views.Comparison;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.*;

@Path(value = "/comparison")
public class ComparisonResources extends Resources {
    private static final Logger logger = LoggerFactory.getLogger(ComparisonResources.class);

    private ComparisonService comparisonService;

    /**
     * Constructor
     *
     * @param globalConfiguration global application configuration
     * @throws UnknownHostException
     */
    public ComparisonResources(GlobalConfiguration globalConfiguration) throws UnknownHostException {
        super(globalConfiguration);
        comparisonService = new ComparisonServiceImpl(globalConfiguration);
        // set GroupDocs license
        try {
            License license = new License();
            license.setLicense(globalConfiguration.getApplication().getLicensePath());
        } catch (Throwable exc) {
            logger.error("Can not verify Comparison license!");
        }
    }

    /**
     * Get comparison page
     * @return template name
     */
    @GET
    public Comparison getView() {
        logger.debug("comparison config: {}", globalConfiguration.getComparison());
        // initiate index page
        return new Comparison(globalConfiguration, DEFAULT_CHARSET);
    }

    /**
     * Get files and directories
     * @return files and directories list
     */
    @POST
    @Path(value = "/loadFileTree")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public List<FileDescriptionEntity> loadFileTree(FileTreeRequest fileTreeRequest){
        return comparisonService.loadFiles(fileTreeRequest);
    }

    /**
     * Download results
     *
     * @param documentGuid unique key of results
     * @param index page number of result images
     * @param ext results file extension
     */
    @GET
    @Path(value = "/downloadDocument")
    @Produces(APPLICATION_OCTET_STREAM)
    public void downloadDocument(@QueryParam("guid") String documentGuid,
                                 @QueryParam("index") Integer index,
                                 @QueryParam("ext") String ext,
                                 @Context HttpServletResponse response) {
        String filePath = comparisonService.calculateResultFileName(documentGuid, index, ext);
        File file = new File(filePath);
        // set response content info
        addFileDownloadHeaders(response, file.getName(), file.length());
        // download the document
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
             ServletOutputStream outputStream = response.getOutputStream()) {

            IOUtils.copy(inputStream, outputStream);
        } catch (Exception ex){
            logger.error("Exception in downloading document", ex);
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Upload document
     * @return uploaded document object (the object contains uploaded document guid)
     */
    @POST
    @Path(value = "/uploadDocument")
    @Produces(APPLICATION_JSON)
    @Consumes(MULTIPART_FORM_DATA)
    public UploadedDocumentEntity uploadDocument(@FormDataParam("file")  InputStream inputStream,
                                                 @FormDataParam("file") FormDataContentDisposition fileDetail,
                                                 @FormDataParam("url") String url,
                                                 @FormDataParam("rewrite") Boolean rewrite) {
        InputStream uploadedInputStream = null;
        try {
            String fileName;
            if (StringUtils.isEmpty(url)) {
                // get the InputStream to store the file
                uploadedInputStream = inputStream;
                fileName = fileDetail.getFileName();
            } else {
                // get the InputStream from the URL
                URL fileUrl = URI.create(url).toURL();
                uploadedInputStream = fileUrl.openStream();
                fileName = FilenameUtils.getName(fileUrl.getPath());
            }
            // get documents storage path
            String documentStoragePath = globalConfiguration.getComparison().getFilesDirectory();
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
        }catch(Exception ex){
            logger.error("Exception in uploading document", ex);
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
     * Compare files from local storage
     *
     * @param compareRequest request with paths to files
     * @return response with compare results
     */
    @POST
    @Path(value = "/compareWithPaths")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public CompareResultResponse compareWithPaths(CompareRequest compareRequest) {
        // check formats
        if (comparisonService.checkFiles(compareRequest.getFirstPath(), compareRequest.getSecondPath())) {
            // compare
            return comparisonService.compare(compareRequest);
        } else {
            logger.error("Document types are different");
            throw new TotalGroupDocsException("Document types are different");
        }
    }

    /**
     * Compare documents from form formats
     *
     * @param firstFileDetail details of first file
     * @param firstInputStream content of first file
     * @param secondFileDetail details of second file
     * @param secondInputStream content of second file
     * @param firstPassword password for first file
     * @param secondPassword password for second file
     * @return response with compare results
     */
    @POST
    @Path(value = "/compareFiles")
    @Produces(APPLICATION_JSON)
    @Consumes(MULTIPART_FORM_DATA)
    public CompareResultResponse compareFiles(@FormDataParam("firstFile")  InputStream firstInputStream,
                                              @FormDataParam("firstFile") FormDataContentDisposition firstFileDetail,
                                              @FormDataParam("secondFile")  InputStream secondInputStream,
                                              @FormDataParam("secondFile") FormDataContentDisposition secondFileDetail,
                                              @FormDataParam("firstPassword") String firstPassword,
                                              @FormDataParam("secondPassword") String secondPassword) {
        String firstFileName = firstFileDetail.getFileName();
        String secondFileName = secondFileDetail.getFileName();
        // check formats
        if (comparisonService.checkFiles(firstFileName, secondFileName)) {
            // compare files
            return comparisonService.compareFiles(firstInputStream, firstPassword, secondInputStream, secondPassword, FilenameUtils.getExtension(firstFileName));
        } else {
            logger.error("Document types are different");
            throw new TotalGroupDocsException("Document types are different");
        }
    }

    /**
     * Compare two files by urls
     *
     * @param compareRequest request with urls to files
     * @return response with compare results
     */
    @POST
    @Path(value = "/compareWithUrls")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public CompareResultResponse compareWithUrls(CompareRequest compareRequest) {
        try {
            String firstPath = compareRequest.getFirstPath();
            String secondPath = compareRequest.getSecondPath();
            // check formats
            if (comparisonService.checkFiles(firstPath, secondPath)) {
                URL fUrl = URI.create(firstPath).toURL();
                URL sUrl = URI.create(secondPath).toURL();

                String firstPassword = compareRequest.getFirstPassword();
                String secondPassword = compareRequest.getSecondPassword();
                // open streams for urls
                try (InputStream firstContent = fUrl.openStream();
                     InputStream secondContent = sUrl.openStream()) {
                    // compare
                    return comparisonService.compareFiles(firstContent, firstPassword, secondContent, secondPassword, FilenameUtils.getExtension(firstPath));
                }
            } else {
                logger.error("Document types are different");
                throw new TotalGroupDocsException("Document types are different");
            }
        } catch (IOException e) {
            logger.error("Exception occurred while compare files by urls.");
            throw new TotalGroupDocsException("Exception occurred while compare files by urls.", e);
        }
    }

    /**
     * Get result page
     * @return result page image
     */
    @POST
    @Path(value = "/loadResultPage")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public LoadedPageEntity loadResultPage(LoadResultPageRequest loadResultPageRequest){
        return comparisonService.loadResultPage(loadResultPageRequest);
    }

    @POST
    @Path(value = "/compare")
    @Produces(APPLICATION_JSON)
    @Consumes(MULTIPART_FORM_DATA)
    public CompareResultResponse compare(@FormDataParam("files") List<FormDataBodyPart> files,
                                         @FormDataParam("files") FormDataContentDisposition fileDetails,
                                         @FormDataParam("passwords") List<String> passwords,
                                         @FormDataParam("urls") List<CompareFileDataRequest> urls,
                                         @FormDataParam("paths") List<CompareFileDataRequest> paths) {
        // calculate total amount of files
        int initialCapacity = files.size() + urls.size() + paths.size();

        if (initialCapacity != 2) {
            throw new TotalGroupDocsException("Comparing is impossible. There are must be 2 files.");
        }
        try {
            // transform all files into input streams
            TransformFiles transformFiles = new TransformFiles(files, passwords, urls, paths, initialCapacity).transformToStreams();
            List<String> fileNames = transformFiles.getFileNames();

            // check formats
            if (comparisonService.checkMultiFiles(fileNames)) {
                // get file extension
                String ext = FilenameUtils.getExtension(fileNames.get(0));

                // compare
                List<InputStream> newFiles = transformFiles.getNewFiles();
                List<String> newPasswords = transformFiles.getNewPasswords();
                return comparisonService.compareFiles(newFiles.get(0), newPasswords.get(0), newFiles.get(1), newPasswords.get(1), ext);
            } else {
                logger.error("Document types are different");
                throw new TotalGroupDocsException("Document types are different");
            }
        } catch (IOException e) {
            logger.error("Exception occurred while compare files.");
            throw new TotalGroupDocsException("Exception occurred while compare files.", e);
        }
    }

    @POST
    @Path(value = "/multiCompare")
    @Produces(APPLICATION_JSON)
    @Consumes(MULTIPART_FORM_DATA)
    public CompareResultResponse multiCompare(@FormDataParam("files") List<FormDataBodyPart> files,
                                              @FormDataParam("files") FormDataContentDisposition fileDetails,
                                              @FormDataParam("passwords") List<String> passwords,
                                              @FormDataParam("urls") List<CompareFileDataRequest> urls,
                                              @FormDataParam("paths") List<CompareFileDataRequest> paths) {
        // calculate total amount of files
        int initialCapacity = files.size() + urls.size() + paths.size();

        if (initialCapacity < 2) {
            throw new TotalGroupDocsException("Comparing is impossible. There are less than 2 files.");
        }
        try {
            // transform all files into input streams
            TransformFiles transformFiles = new TransformFiles(files, passwords, urls, paths, initialCapacity).transformToStreams();
            List<String> fileNames = transformFiles.getFileNames();

            // check formats
            if (comparisonService.checkMultiFiles(fileNames)) {
                // get file extension
                String ext = FilenameUtils.getExtension(fileNames.get(0));

                // compare
                return comparisonService.multiCompareFiles(transformFiles.getNewFiles(), transformFiles.getNewPasswords(), ext);
            } else {
                logger.error("Document types are different");
                throw new TotalGroupDocsException("Document types are different");
            }
        } catch (IOException e) {
            logger.error("Exception occurred while multi compare files by streams.");
            throw new TotalGroupDocsException("Exception occurred while multi compare files by streams.", e);
        }
    }

    private class TransformFiles {
        private List<FormDataBodyPart> files;
        private List<String> passwords;
        private List<CompareFileDataRequest> urls;
        private List<CompareFileDataRequest> paths;
        private int initialCapacity;
        private List<InputStream> newFiles;
        private List<String> fileNames;
        private List<String> newPasswords;

        public TransformFiles(List<FormDataBodyPart> files, List<String> passwords, List<CompareFileDataRequest> urls, List<CompareFileDataRequest> paths, int initialCapacity) {
            this.files = files;
            this.passwords = passwords;
            this.urls = urls;
            this.paths = paths;
            this.initialCapacity = initialCapacity;
        }

        public List<InputStream> getNewFiles() {
            return newFiles;
        }

        public List<String> getFileNames() {
            return fileNames;
        }

        public List<String> getNewPasswords() {
            return newPasswords;
        }

        public TransformFiles transformToStreams() throws IOException {
            newFiles = new ArrayList<>(initialCapacity);
            fileNames = new ArrayList<>(initialCapacity);
            newPasswords = new ArrayList<>(initialCapacity);

            // transform files
            int index = 0;
            for (FormDataBodyPart file: files) {
                fileNames.add(file.getContentDisposition().getFileName());
                newFiles.add(((BodyPartEntity) file.getEntity()).getInputStream());
                newPasswords.add(passwords.get(index));
                index++;
            }

            // transform urls
            for (CompareFileDataRequest urlRequest: urls) {
                String file = urlRequest.getFile();
                fileNames.add(file);
                URL url = URI.create(file).toURL();
                newFiles.add(url.openStream());
                newPasswords.add(urlRequest.getPassword());
            }

            // transform paths
            for (CompareFileDataRequest pathRequest: paths) {
                String file = pathRequest.getFile();
                fileNames.add(file);
                newFiles.add(new FileInputStream(file));
                newPasswords.add(pathRequest.getPassword());
            }
            return this;
        }
    }
}
