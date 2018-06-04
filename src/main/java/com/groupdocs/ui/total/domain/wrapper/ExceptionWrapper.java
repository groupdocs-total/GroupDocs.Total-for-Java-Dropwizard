package com.groupdocs.ui.total.domain.wrapper;

/**
 * ExceptionWrapper
 *
 * @author Aspose Pty Ltd
 */
public class ExceptionWrapper {
    private String message;
    private Exception exception;

    /**
     * Get exception message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set exception message
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get exception
     * @return exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Set exception
     * @param exception exception
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

}
