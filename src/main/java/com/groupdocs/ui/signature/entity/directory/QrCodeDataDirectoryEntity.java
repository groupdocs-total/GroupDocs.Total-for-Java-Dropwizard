package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;

public class QrCodeDataDirectoryEntity extends DataDirectoryEntity {

    public QrCodeDataDirectoryEntity(SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration, "/QrCodes");
    }
}
