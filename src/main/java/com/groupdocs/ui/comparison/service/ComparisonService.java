package com.groupdocs.ui.comparison.service;

import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.PageDescriptionEntity;
import com.groupdocs.ui.common.entity.web.request.FileTreeRequest;
import com.groupdocs.ui.comparison.model.request.CompareRequest;
import com.groupdocs.ui.comparison.model.request.LoadResultPageRequest;
import com.groupdocs.ui.comparison.model.response.CompareResultResponse;

import java.io.InputStream;
import java.util.List;

public interface ComparisonService {

    /**
     * Load list of elements from directory
     *
     * @param fileTreeRequest request with path to directory
     * @return list of files and folders
     */
    List<FileDescriptionEntity> loadFiles(FileTreeRequest fileTreeRequest);

    /**
     * Compare two documents, save results in files,
     * return result descriptions and paths to result files
     *
     * @param compareRequest request with paths to documents to compare
     * @return comparing results
     */
    CompareResultResponse compare(CompareRequest compareRequest);

    /**
     * Compare two documents, save results in files,
     * return result descriptions and paths to result files
     *
     * @param firstContent stream for first document
     * @param firstPassword
     * @param secondContent stream for second document
     * @param secondPassword
     * @param fileExt file extension (for saving result file)
     * @return comparing results
     */
    CompareResultResponse compareFiles(InputStream firstContent, String firstPassword, InputStream secondContent, String secondPassword, String fileExt);

    /**
     * Load the page of results
     *
     * @param loadResultPageRequest request with path to page result
     * @return page result data
     */
    PageDescriptionEntity loadResultPage(LoadResultPageRequest loadResultPageRequest);

    /**
     * Produce file names for results
     *
     * @param documentGuid unique key of results
     * @param index page number, if it is null, return file name for all-pages result file
     * @param ext result file extension
     * @return full path for result file
     */
    String calculateResultFileName(String documentGuid, Integer index, String ext);

    /**
     * Check format files for comparing
     *
     * @param firstFileName first file name
     * @param secondFileName second file name
     * @return true - formats of the both files are the same and format is supported, false - other
     */
    boolean checkFiles(String firstFileName, String secondFileName);

    /**
     * Compare several files
     *
     * @param files list of input streams for files
     * @param passwords list of passwords for files
     * @param ext result file extension
     * @return comparing results
     */
    CompareResultResponse multiCompareFiles(List<InputStream> files, List<String> passwords, String ext);

    /**
     * Check format files for comparing
     *
     * @param fileNames the list of paths to files
     * @return true - formats of all the files are the same and format is supported, false - other
     */
    boolean checkMultiFiles(List<String> fileNames);
}
