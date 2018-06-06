package com.groupdocs.ui.signature.domain.wrapper;

import com.groupdocs.ui.common.domain.wrapper.UploadedDocumentWrapper;

public class UploadedSignatureWrapper extends UploadedDocumentWrapper {
    private String signatureImage;

    public String getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }
}
