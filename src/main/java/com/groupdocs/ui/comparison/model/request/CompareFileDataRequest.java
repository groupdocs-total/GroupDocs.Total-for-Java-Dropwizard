package com.groupdocs.ui.comparison.model.request;

public class CompareFileDataRequest {
    /**
     * URL or path to file
     */
    private String file;
    /**
     * Password for the file
     */
    private String password;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
