package com.groupdocs.ui.signature.util.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

import java.io.File;

/**
 * OutputDirectoryUtils
 * Compare and sort file types - folders first
 * @author Aspose Pty Ltd
 */
public class OutputDirectoryUtils implements IDirectoryUtils {
    private final String OUTPUT_FOLDER = File.separator + "Output";
    private SignatureConfiguration signatureConfiguration;

    /**
     * Constructor
     * @param signatureConfiguration
     */
    public OutputDirectoryUtils(SignatureConfiguration signatureConfiguration){
        this.signatureConfiguration = signatureConfiguration;

        // create output directories
        if(signatureConfiguration.getOutputDirectory() == null || signatureConfiguration.getOutputDirectory().isEmpty()){
            signatureConfiguration.setOutputDirectory(signatureConfiguration.getFilesDirectory() + OUTPUT_FOLDER);
        }
    }

    /**
     * Get Output directory path
     * @return path for folder where to save signed documents
     */
    @Override
    public String getPath() {
        return signatureConfiguration.getOutputDirectory();
    }
}
