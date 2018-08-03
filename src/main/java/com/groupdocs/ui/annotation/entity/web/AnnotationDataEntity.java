package com.groupdocs.ui.annotation.entity.web;

/**
 * AnnotationDataEntity
 *
 * @author Aspose Pty Ltd
 */
public class AnnotationDataEntity {
    private double id;
    private double left;
    private double top;
    private double width;
    private double height;
    private int pageNumber;
    private String svgPath;
    private String type;
    private String documentType;
    private CommentsEntity[] comments;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSvgPath() {
        return svgPath;
    }

    public void setSvgPath(String svgPath) {
        this.svgPath = svgPath;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public CommentsEntity[] getComments() {
        return comments;
    }

    public void setComments(CommentsEntity[] comments) {
        this.comments = comments;
    }
}
