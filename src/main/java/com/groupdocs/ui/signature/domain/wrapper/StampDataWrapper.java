package com.groupdocs.ui.signature.domain.wrapper;

public class StampDataWrapper {
    private String text;
    private int fontSize;
    private int textRepeat;
    private String font;
    private int radius;
    private String textColor;
    private String srokeColor;
    private String backgroundColor;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getSrokeColor() {
        return srokeColor;
    }

    public void setSrokeColor(String srokeColor) {
        this.srokeColor = srokeColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
