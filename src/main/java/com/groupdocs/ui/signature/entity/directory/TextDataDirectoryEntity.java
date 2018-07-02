package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;

public class TextDataDirectoryEntity extends DataDirectoryEntity {

    public TextDataDirectoryEntity(SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration, "/Text");
    }
}
