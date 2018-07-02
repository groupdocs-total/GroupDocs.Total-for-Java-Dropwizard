package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;

public class ImageDataDirectoryEntity extends DataDirectoryEntity {

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
