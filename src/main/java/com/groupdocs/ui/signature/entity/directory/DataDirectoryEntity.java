package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

public abstract class DataDirectoryEntity {
    public static final String DATA_PREVIEW_FOLDER = "/Preview";
    public static final String DATA_XML_FOLDER = "/XML";
    protected SignatureConfiguration signatureConfiguration;
    protected String currentDirectoryPath;

    public DataDirectoryEntity(SignatureConfiguration signatureConfiguration, String currentDirectoryPath) {
        this.signatureConfiguration = signatureConfiguration;
        this.currentDirectoryPath = currentDirectoryPath;
    }

    public String getPath(){
        return signatureConfiguration.getDataDirectory() + currentDirectoryPath;
    }

    public String getPreviewPath(){
        return signatureConfiguration.getDataDirectory() + currentDirectoryPath + DATA_PREVIEW_FOLDER;
    }

    public String getXmlPath(){
        return signatureConfiguration.getDataDirectory() + currentDirectoryPath + DATA_XML_FOLDER;
    }
}
