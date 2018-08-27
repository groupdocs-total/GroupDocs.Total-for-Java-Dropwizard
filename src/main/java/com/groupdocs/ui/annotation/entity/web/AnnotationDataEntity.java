package com.groupdocs.ui.annotation.entity.web;

/**
 * AnnotationDataEntity
 *
 * @author Aspose Pty Ltd
 */
public class AnnotationDataEntity {
    private int id;
    private int pageNumber;
    private int fontSize;
    private double left;
    private double top;
    private double width;
    private double height;
    private String svgPath;
    private String type;
    private String documentType;
    private String text;
    private String font;
    private CommentsEntity[] comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
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

    public String getSvgPath() {
        return svgPath;
    }

    public void setSvgPath(String svgPath) {
        this.svgPath = svgPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public CommentsEntity[] getComments() {
        return comments;
    }

    public void setComments(CommentsEntity[] comments) {
        this.comments = comments;
    }
}
