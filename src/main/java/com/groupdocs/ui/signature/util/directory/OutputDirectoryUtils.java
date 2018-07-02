package com.groupdocs.ui.signature.util.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

public class OutputDirectoryUtils implements IDirectoryUtils {
    private final String OUTPUT_FOLDER = "/Output";
    private SignatureConfiguration signatureConfiguration;

    public OutputDirectoryUtils(SignatureConfiguration signatureConfiguration){
        this.signatureConfiguration = signatureConfiguration;

        // create output directories
        if(signatureConfiguration.getOutputDirectory() == null || signatureConfiguration.getOutputDirectory().isEmpty()){
            signatureConfiguration.setOutputDirectory(signatureConfiguration.getFilesDirectory() + OUTPUT_FOLDER);
        }
    }

    @Override
    public String getPath() {
        return signatureConfiguration.getOutputDirectory();
    }
}
