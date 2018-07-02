package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;

public class StampDataDirectoryEntity extends DataDirectoryEntity {

    public StampDataDirectoryEntity(SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration, "/Stamps");
    }
}
