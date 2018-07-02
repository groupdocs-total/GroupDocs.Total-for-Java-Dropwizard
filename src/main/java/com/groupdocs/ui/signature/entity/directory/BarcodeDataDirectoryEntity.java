package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;

public class BarcodeDataDirectoryEntity extends DataDirectoryEntity {

    public BarcodeDataDirectoryEntity(SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration, "/BarCodes");
    }
}
