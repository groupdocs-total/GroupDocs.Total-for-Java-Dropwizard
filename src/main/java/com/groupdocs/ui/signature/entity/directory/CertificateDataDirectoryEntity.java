package com.groupdocs.ui.signature.entity.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;

/**
 * CertificateDataDirectoryEntity
 *
 * @author Aspose Pty Ltd
 */
public class CertificateDataDirectoryEntity extends DataDirectoryEntity {

    /**
     * Constructor
     * @param signatureConfiguration signature configuration object
     */
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
