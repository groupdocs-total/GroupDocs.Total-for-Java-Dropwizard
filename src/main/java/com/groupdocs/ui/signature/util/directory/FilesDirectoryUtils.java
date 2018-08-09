package com.groupdocs.ui.signature.util.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

import java.io.File;

/**
 * FilesDirectoryUtils
 * Compare and sort file types - folders first
 * @author Aspose Pty Ltd
 */
public class FilesDirectoryUtils implements IDirectoryUtils {
    private SignatureConfiguration signatureConfiguration;

    /**
     * Constructor
     * @param signatureConfiguration
     */
    public FilesDirectoryUtils(SignatureConfiguration signatureConfiguration){
        this.signatureConfiguration = signatureConfiguration;
    }

    /**
     * Get path for files directory
     * @return path of the files directory
     */
    @Override
    public String getPath() {
        return signatureConfiguration.getFilesDirectory();
    }

}
