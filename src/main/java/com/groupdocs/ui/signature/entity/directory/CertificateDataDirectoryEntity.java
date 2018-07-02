package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.DataDirectoryEntity;

public class CertificateDataDirectoryEntity extends DataDirectoryEntity {

    public CertificateDataDirectoryEntity(SignatureConfiguration signatureConfiguration){
        super(signatureConfiguration, "/Certificates");
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
