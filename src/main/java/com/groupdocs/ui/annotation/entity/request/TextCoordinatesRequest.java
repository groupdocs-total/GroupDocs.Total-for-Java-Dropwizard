package com.groupdocs.ui.annotation.entity.request;

/**
 * TextCoordinatesRequest
 *
 * @author Aspose Pty Ltd
 */
public class TextCoordinatesRequest {
    /**
     * Guid of document
     */
    private String guid;
    /**
     * Password of document
     */
    private String password;
    /**
     * The number of page in document
     */
    private int pageNumber;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
