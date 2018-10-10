package com.groupdocs.ui.comparison.service;

import com.google.common.collect.Ordering;
import com.groupdocs.comparison.Comparer;
import com.groupdocs.comparison.MultiComparer;
import com.groupdocs.comparison.common.changes.ChangeInfo;
import com.groupdocs.comparison.common.compareresult.ICompareResult;
import com.groupdocs.comparison.common.comparisonsettings.ComparisonSettings;
import com.groupdocs.comparison.internal.c.a.m.System.e.q;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.LoadedPageEntity;
import com.groupdocs.ui.common.entity.web.request.FileTreeRequest;
import com.groupdocs.ui.common.exception.TotalGroupDocsException;
import com.groupdocs.ui.common.util.comparator.FileNameComparator;
import com.groupdocs.ui.common.util.comparator.FileTypeComparator;
import com.groupdocs.ui.comparison.config.ComparisonConfiguration;
import com.groupdocs.ui.comparison.model.request.CompareRequest;
import com.groupdocs.ui.comparison.model.request.LoadResultPageRequest;
import com.groupdocs.ui.comparison.model.response.CompareResultResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ComparisonServiceImpl implements ComparisonService {

    private static final Logger logger = LoggerFactory.getLogger(ComparisonServiceImpl.class);
    public static final String COMPARE_RESULT = "compareResult";
    public static final String JPG = "jpg";
    public static final String DOCX = "docx";
    public static final String DOC = "doc";
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String PPT = "ppt";
    public static final String PPTX = "pptx";
    public static final String PDF = "pdf";
    public static final String TXT = "txt";

    private GlobalConfiguration globalConfiguration;

    public ComparisonServiceImpl(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
        // check files directories
        ComparisonConfiguration comparisonConfiguration = globalConfiguration.getComparison();
        if (StringUtils.isEmpty(comparisonConfiguration.getFilesDirectory())) {
            logger.error("Files directory must be specified!");
            throw new IllegalStateException("Files directory must be specified!");
        } else {
            new File(comparisonConfiguration.getFilesDirectory()).mkdirs();
            if (!StringUtils.isEmpty(comparisonConfiguration.getResultDirectory())) {
                new File(comparisonConfiguration.getResultDirectory()).mkdirs();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileDescriptionEntity> loadFiles(FileTreeRequest fileTreeRequest) {
        String currentPath = fileTreeRequest.getPath();
        if (StringUtils.isEmpty(currentPath)) {
            currentPath = globalConfiguration.getComparison().getFilesDirectory();
        } else {
            currentPath = String.format("%s%s%s", globalConfiguration.getComparison().getFilesDirectory(), File.separator, currentPath);
        }
        File directory = new File(currentPath);
        List<FileDescriptionEntity> fileList = new ArrayList<>();
        List<File> filesList = Arrays.asList(directory.listFiles());
        try {
            // sort list of files and folders
            filesList = Ordering.from(FileTypeComparator.instance).compound(FileNameComparator.instance).sortedCopy(filesList);
            for (File file : filesList) {
                // check if current file/folder is hidden
                if (file.isHidden()) {
                    // ignore current file and skip to next one
                    continue;
                } else {
                    FileDescriptionEntity fileDescription = getFileDescriptionEntity(file);
                    // add object to array list
                    fileList.add(fileDescription);
                }
            }
            return fileList;
        } catch (Exception ex) {
            logger.error("Exception occurred while load file tree");
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Create file description
     *
     * @param file file
     * @return file description
     */
    private FileDescriptionEntity getFileDescriptionEntity(File file) {
        FileDescriptionEntity fileDescription = new FileDescriptionEntity();
        // set path to file
        fileDescription.setGuid(file.getAbsolutePath());
        // set file name
        fileDescription.setName(file.getName());
        // set is directory true/false
        fileDescription.setDirectory(file.isDirectory());
        // set file size
        fileDescription.setSize(file.length());
        return fileDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompareResultResponse compare(CompareRequest compareRequest) {
        String firstPath = compareRequest.getFirstPath();

        ICompareResult compareResult;

        //TODO: remove this synchronization when the bug COMPARISONJAVA-436 is fixed
        synchronized (this) {
            // create new comparer
            Comparer comparer = new Comparer();
            // create setting for comparing
            ComparisonSettings settings = new ComparisonSettings();

            // compare two documents
            compareResult = comparer.compare(firstPath,
                    convertEmptyPasswordToNull(compareRequest.getFirstPassword()),
                    compareRequest.getSecondPath(),
                    convertEmptyPasswordToNull(compareRequest.getSecondPassword()),
                    settings);
        }

        if (compareResult == null) {
            throw new TotalGroupDocsException("Something went wrong. We've got null result.");
        }

        // convert results
        CompareResultResponse compareResultResponse = getCompareResultResponse(compareResult);

        //save all results in file
        String extension = FilenameUtils.getExtension(firstPath);
        saveFile(compareResultResponse.getGuid(), null, compareResult.getStream(), extension);

        compareResultResponse.setExtension(extension);

        return compareResultResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompareResultResponse compareFiles(InputStream firstContent, String firstPassword, InputStream secondContent, String secondPassword, String fileExt) {

        ICompareResult compareResult;

        //TODO: remove this synchronization when the bug COMPARISONJAVA-436 is fixed
        synchronized (this) {
            // create new comparer
            Comparer comparer = new Comparer();
            // create setting for comparing
            ComparisonSettings settings = new ComparisonSettings();

            // compare two documents
            compareResult = comparer.compare(firstContent,
                    convertEmptyPasswordToNull(firstPassword),
                    secondContent,
                    convertEmptyPasswordToNull(secondPassword),
                    settings);
        }

        if (compareResult == null) {
            throw new TotalGroupDocsException("Something went wrong. We've got null result.");
        }

        // convert results
        CompareResultResponse compareResultResponse = getCompareResultResponse(compareResult);

        //save all results in file
        saveFile(compareResultResponse.getGuid(), null, compareResult.getStream(), fileExt);

        compareResultResponse.setExtension(fileExt);

        return compareResultResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadedPageEntity loadResultPage(LoadResultPageRequest loadResultPageRequest) {
        LoadedPageEntity loadedPage = new LoadedPageEntity();

        // load file with results
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(loadResultPageRequest.getPath()))) {

            byte[] bytes = IOUtils.toByteArray(inputStream);
            // encode ByteArray into String
            String encodedImage = Base64.getEncoder().encodeToString(bytes);
            loadedPage.setPageImage(encodedImage);

        } catch (Exception ex) {
            logger.error("Exception occurred while loading result page", ex);
            throw new TotalGroupDocsException("Exception occurred while loading result page", ex);
        }

        return loadedPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculateResultFileName(String documentGuid, Integer index, String ext) {
        // configure file name for results
        String directory = globalConfiguration.getComparison().getResultDirectory();
        String resultDirectory = StringUtils.isEmpty(directory) ? globalConfiguration.getComparison().getFilesDirectory() : directory;
        String extension = ext != null ? getRightExt(ext.toLowerCase()) : "";
        // for images of pages specify index, for all result pages file specify "all" prefix
        String idx = index == null ? "all." : index.toString() + ".";
        String suffix = idx + extension;
        return String.format("%s%s%s-%s-%s", resultDirectory, File.separator, COMPARE_RESULT, documentGuid, suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkFiles(String firstFile, String secondFile) {
        String extension = FilenameUtils.getExtension(firstFile);
        // check if files extensions are the same and support format file
        return extension.equals(FilenameUtils.getExtension(secondFile)) && checkSupportedFiles(extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompareResultResponse multiCompareFiles(List<InputStream> files, List<String> passwords, String ext) {

        ICompareResult compareResult;

        //TODO: remove this synchronization when the bug COMPARISONJAVA-436 is fixed
        synchronized (this) {
            // create new comparer
            MultiComparer multiComparer = new MultiComparer();
            // create setting for comparing
            ComparisonSettings settings = new ComparisonSettings();

            // transform lists of files and passwords
            List<q> newFiles = new ArrayList<>();
            List<String> newPasswords = new ArrayList<>();
            for (int i = 1; i < files.size(); i++) {
                newFiles.add(q.E(files.get(i)));
                newPasswords.add(passwords.get(i));
            }

            // compare two documents
            compareResult = multiComparer.compare(files.get(0), passwords.get(0), newFiles, newPasswords,
                    settings);
        }

        if (compareResult == null) {
            throw new TotalGroupDocsException("Something went wrong. We've got null result.");
        }

        // convert results
        CompareResultResponse compareResultResponse = getCompareResultResponse(compareResult);

        //save all results in file
        saveFile(compareResultResponse.getGuid(), null, compareResult.getStream(), ext);

        compareResultResponse.setExtension(ext);

        return compareResultResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkMultiFiles(List<String> fileNames) {
        String extension = FilenameUtils.getExtension(fileNames.get(0));
        // check if files extensions are the same and support format file
        if (! checkSupportedFiles(extension)) {
            return false;
        }
        for (String path : fileNames) {
            if (! extension.equals(FilenameUtils.getExtension(path))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Replace empty string with null
     *
     * @param password
     * @return password or null if password is empty
     */
    private String convertEmptyPasswordToNull(String password) {
        return StringUtils.isEmpty(password) ? null : password;
    }

    /**
     * Check support formats for comparing
     *
     * @param extension file extension
     * @return true - format is supported, false - format is not supported
     */
    private boolean checkSupportedFiles(String extension) {
        switch (extension) {
            case DOC:
            case DOCX:
            case XLS:
            case XLSX:
            case PPT:
            case PPTX:
            case PDF:
            case TXT:
                return true;
            default:
                return false;
        }
    }

    /**
     * Convert results of comparing and save result files
     *
     * @param compareResult results
     * @return results response
     */
    private CompareResultResponse getCompareResultResponse(ICompareResult compareResult) {
        CompareResultResponse compareResultResponse = new CompareResultResponse();

        // list of changes
        ChangeInfo[] changes = compareResult.getChanges();
        compareResultResponse.setChanges(changes);

        String guid = UUID.randomUUID().toString();
        compareResultResponse.setGuid(guid);

        // if there are changes save images of all pages
        // unless save only the last page with summary
        if (changes != null && changes.length > 0) {
            List<String> pages = saveImages(compareResult.getImages(), guid);
            // save all pages
            compareResultResponse.setPages(pages);
        } else {
            List<InputStream> images = compareResult.getImages();
            int last = images.size() - 1;
            // save only summary page
            compareResultResponse.setPages(Collections.singletonList(saveFile(guid, last, images.get(last), JPG)));
        }
        return compareResultResponse;
    }

    /**
     * Save images with results
     *
     * @param images list of streams
     * @param guid   unique key of results
     * @return list of paths to saved images
     */
    private List<String> saveImages(List<InputStream> images, String guid) {
        List<String> paths = new ArrayList<>(images.size());
        for (int i = 0; i < images.size(); i++) {
            paths.add(saveFile(guid, i, images.get(i), JPG));
        }
        return paths;
    }

    /**
     * Save file
     *
     * @param guid        unique key of results
     * @param pageNumber  result's page number
     * @param inputStream stream for saving
     * @param ext         result file extension
     * @return path to saved file
     */
    private String saveFile(String guid, Integer pageNumber, InputStream inputStream, String ext) {
        String imageFileName = calculateResultFileName(guid, pageNumber, ext);
        try {
            Files.copy(inputStream, Paths.get(imageFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Exception occurred while write result images files.");
        }
        return imageFileName;
    }

    /**
     * Fix file extensions for some formats
     *
     * @param ext extension string
     * @return right extension for result file
     */
    private String getRightExt(String ext) {
        switch (ext) {
            case DOC:
            case DOCX:
                return DOCX;
            case XLS:
            case XLSX:
                return XLSX;
            case PPT:
            case PPTX:
                return PPTX;
            default:
                return ext;
        }
    }
}
