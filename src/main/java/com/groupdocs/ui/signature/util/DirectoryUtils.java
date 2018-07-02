package com.groupdocs.ui.signature.util;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

public class DirectoryUtils {
    private FilesDirectoryUtils filesDirectory;
    private OutputDirectoryUtils outputDirectory;
    private DataDirectoryUtils dataDirectory;

    public DirectoryUtils(SignatureConfiguration signatureConfiguration){
        filesDirectory = new FilesDirectoryUtils(signatureConfiguration);
        outputDirectory = new OutputDirectoryUtils(signatureConfiguration);
        dataDirectory = new DataDirectoryUtils(signatureConfiguration);
    }

    public FilesDirectoryUtils getFilesDirectory() {
        return filesDirectory;
    }

    public OutputDirectoryUtils getOutputDirectory() {
        return outputDirectory;
    }

    public DataDirectoryUtils getDataDirectory() {
        return dataDirectory;
    }
}
