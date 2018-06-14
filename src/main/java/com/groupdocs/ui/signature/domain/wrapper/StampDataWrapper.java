package com.groupdocs.ui.signature.domain.wrapper;

import java.awt.*;

public class StampDataWrapper {
    private String text;
    private String textExpansion;
    private String font;
    private String imageGuid;
    private String textColor = "rgb(0,0,0)";
    private String strokeColor = "rgb(0,0,0)";
    private String backgroundColor = "rgb(0,0,0)";
    private int radius;
    private int fontSize;
    private int textRepeat;
    private int width;
    private int height;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextExpansion() {
        return textExpansion;
    }

    public void setTextExpansion(String textExpansion) {
        this.textExpansion = textExpansion;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getImageGuid() {
        return imageGuid;
    }

    public void setImageGuid(String imageGuid) {
        this.imageGuid = imageGuid;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getTextRepeat() {
        return textRepeat;
    }

    public void setTextRepeat(int textRepeat) {
        this.textRepeat = textRepeat;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
