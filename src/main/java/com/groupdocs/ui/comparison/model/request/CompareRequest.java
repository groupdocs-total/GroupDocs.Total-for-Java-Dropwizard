package com.groupdocs.ui.comparison.model.request;

public class CompareRequest {
    /**
     * path or url for first file
     */
    private String firstPath;
    /**
     * path or url for second file
     */
    private String secondPath;
    /**
     * Password for first file
     */
    private String firstPassword;
    /**
     * Password for second file
     */
    private String secondPassword;

    public String getFirstPath() {
        return firstPath;
    }

    public void setFirstPath(String firstPath) {
        this.firstPath = firstPath;
    }

    public String getSecondPath() {
        return secondPath;
    }

    public void setSecondPath(String secondPath) {
        this.secondPath = secondPath;
    }

    public String getFirstPassword() {
        return firstPassword;
    }

    public void setFirstPassword(String firstPassword) {
        this.firstPassword = firstPassword;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }
}
