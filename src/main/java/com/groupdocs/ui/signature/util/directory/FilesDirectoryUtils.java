package com.groupdocs.ui.signature.util.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

import java.io.File;

public class FilesDirectoryUtils implements IDirectoryUtils {
    private SignatureConfiguration signatureConfiguration;

    public FilesDirectoryUtils(SignatureConfiguration signatureConfiguration){
        this.signatureConfiguration = signatureConfiguration;

        // set files directory
        if(!new File(signatureConfiguration.getFilesDirectory()).exists()) {
            signatureConfiguration.setFilesDirectory(new File("").getAbsolutePath() + signatureConfiguration.getFilesDirectory());
        }
    }

    @Override
    public String getPath() {
        return signatureConfiguration.getFilesDirectory();
    }

}
