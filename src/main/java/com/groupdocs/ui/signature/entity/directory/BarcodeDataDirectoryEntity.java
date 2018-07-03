package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

/**
 * BarcodeDataDirectoryEntity
 *
 * @author Aspose Pty Ltd
 */
public class BarcodeDataDirectoryEntity extends DataDirectoryEntity {

    /**
     * Constructor
     * @param signatureConfiguration signature configuration object
     */
    public BarcodeDataDirectoryEntity(SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration, "/BarCodes");
    }
}
