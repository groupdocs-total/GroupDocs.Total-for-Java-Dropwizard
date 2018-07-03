package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

/**
 * ImageDataDirectoryEntity
 *
 * @author Aspose Pty Ltd
 */
public class ImageDataDirectoryEntity extends DataDirectoryEntity {

    /**
     * Constructor
     * @param signatureConfiguration signature configuration object
     */
    public ImageDataDirectoryEntity(SignatureConfiguration signatureConfiguration){
        super(signatureConfiguration, "/Image");
    }

    @Override
    public String getPreviewPath() {
        return "";
    }

    @Override
    public String getXmlPath() {
        return "";
    }

}
