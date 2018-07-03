package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

/**
 * QrCodeDataDirectoryEntity
 *
 * @author Aspose Pty Ltd
 */
public class QrCodeDataDirectoryEntity extends DataDirectoryEntity {

    /**
     * Constructor
     * @param signatureConfiguration signature configuration object
     */
    public QrCodeDataDirectoryEntity(SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration, "/QrCodes");
    }
}
