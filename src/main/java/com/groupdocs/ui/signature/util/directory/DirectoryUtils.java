package com.groupdocs.ui.signature.util.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

/**
 * DirectoryUtils
 * Compare and sort file types - folders first
 * @author Aspose Pty Ltd
 */
public class DirectoryUtils {
    private FilesDirectoryUtils filesDirectory;
    private DataDirectoryUtils dataDirectory;

    /**
     * Constructor
     * @param signatureConfiguration
     */
    public DirectoryUtils(SignatureConfiguration signatureConfiguration){
        filesDirectory = new FilesDirectoryUtils(signatureConfiguration);
        dataDirectory = new DataDirectoryUtils(signatureConfiguration);
    }

    /**
     * Get files directory - path where all documents for signing are stored
     * @return FilesDirectoryUtils
     */
    public FilesDirectoryUtils getFilesDirectory() {
        return filesDirectory;
    }

    /**
     * Get data directory - path where all signatures are stored
     * @return DataDirectoryUtils
     */
    public DataDirectoryUtils getDataDirectory() {
        return dataDirectory;
    }
}
