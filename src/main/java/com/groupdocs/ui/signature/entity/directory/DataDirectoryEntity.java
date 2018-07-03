package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

/**
 * DataDirectoryEntity
 *
 * @author Aspose Pty Ltd
 */
public abstract class DataDirectoryEntity {
    public static final String DATA_PREVIEW_FOLDER = "/Preview";
    public static final String DATA_XML_FOLDER = "/XML";
    protected SignatureConfiguration signatureConfiguration;
    protected String currentDirectoryPath;

    /**
     * Constructor
     * @param signatureConfiguration signature configuration object
     */
    public DataDirectoryEntity(SignatureConfiguration signatureConfiguration, String currentDirectoryPath) {
        this.signatureConfiguration = signatureConfiguration;
        this.currentDirectoryPath = currentDirectoryPath;
    }

    /**
     * Get current directory path
     * @return path for current directory inside the data directory
     */
    public String getPath(){
        return signatureConfiguration.getDataDirectory() + currentDirectoryPath;
    }

    /**
     * Get preview path
     * @return path for current signature type previews
     */
    public String getPreviewPath(){
        return signatureConfiguration.getDataDirectory() + currentDirectoryPath + DATA_PREVIEW_FOLDER;
    }

    /**
     * Get XML path
     * @return path for current signature XML directory
     */
    public String getXmlPath(){
        return signatureConfiguration.getDataDirectory() + currentDirectoryPath + DATA_XML_FOLDER;
    }
}
