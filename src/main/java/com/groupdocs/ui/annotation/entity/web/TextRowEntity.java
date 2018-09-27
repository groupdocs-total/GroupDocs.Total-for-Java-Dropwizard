package com.groupdocs.ui.annotation.entity.web;

import java.util.List;

/**
 * TextRowEntity
 *
 * @author Aspose Pty Ltd
 */
public class TextRowEntity {
    private List<Double> textCoordinates;
    private double lineTop;
    private double lineHeight;

    public List<Double> getTextCoordinates() {
        return textCoordinates;
    }

    public void setTextCoordinates(List<Double> textCoordinates) {
        this.textCoordinates = textCoordinates;
    }

    public double getLineTop() {
        return lineTop;
    }

    public void setLineTop(double lineTop) {
        this.lineTop = lineTop;
    }

    public double getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(double lineHeight) {
        this.lineHeight = lineHeight;
    }
}
