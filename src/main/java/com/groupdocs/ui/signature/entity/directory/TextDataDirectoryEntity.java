package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

/**
 * TextDataDirectoryEntity
 *
 * @author Aspose Pty Ltd
 */
public class TextDataDirectoryEntity extends DataDirectoryEntity {

    /**
     * Constructor
     * @param signatureConfiguration signature configuration object
     */
    public TextDataDirectoryEntity(SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration, "/Text");
    }
}
