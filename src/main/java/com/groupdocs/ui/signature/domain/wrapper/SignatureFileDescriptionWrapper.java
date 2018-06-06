package com.groupdocs.ui.signature.domain.wrapper;

import com.groupdocs.ui.common.domain.wrapper.FileDescriptionWrapper;

public class SignatureFileDescriptionWrapper extends FileDescriptionWrapper {
    private String image;

    /**
     * Get incoded image Base64 string
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * Set incoded image Base64 string
     */
    public void setImage(String image) {
        this.image = image;
    }
}
