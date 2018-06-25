package com.groupdocs.ui.signature.Signer;

import com.groupdocs.signature.domain.enums.PdfTextSignatureImplementation;
import com.groupdocs.signature.options.appearances.PdfTextAnnotationAppearance;
import com.groupdocs.signature.options.textsignature.ImagesSignTextOptions;
import com.groupdocs.signature.options.textsignature.PdfSignTextOptions;
import com.groupdocs.ui.signature.domain.wrapper.SignatureDataWrapper;
import com.groupdocs.ui.signature.domain.wrapper.TextDataWrapper;

import java.awt.Color;

/**
 * StampSigner
 * Signs documents with the stamp signature
 * @author Aspose Pty Ltd
 */
public class TextSigner {
    private TextDataWrapper textData;
    private SignatureDataWrapper signatureData;

    public TextSigner(TextDataWrapper textData, SignatureDataWrapper signatureData){
        this.textData = textData;
        this.signatureData = signatureData;
    }

    public PdfSignTextOptions signPdf(){
        PdfSignTextOptions signOptions = new PdfSignTextOptions(textData.getText());
        signOptions.setLeft(signatureData.getLeft());
        signOptions.setTop(signatureData.getTop());
        signOptions.setHeight(signatureData.getImageHeight());
        signOptions.setWidth(signatureData.getImageWidth());
        // setup colors settings
        signOptions.setBackgroundColor(getColor(textData.getBackgroundColor()));
        // setup text color
        signOptions.setForeColor(getColor(textData.getFontColor()));
        // setup Font options
        signOptions.getFont().setBold(textData.getBold());
        signOptions.getFont().setItalic(textData.getItalic());
        signOptions.getFont().setUnderline(textData.getUnderline());
        signOptions.getFont().setFontFamily(textData.getFont());
        signOptions.getFont().setFontSize(textData.getFontSize());
        //type of implementation
        signOptions.setSignatureImplementation(PdfTextSignatureImplementation.Annotation);
        // specify extended appearance options
        PdfTextAnnotationAppearance appearance = new PdfTextAnnotationAppearance();
        appearance.setBorderColor(getColor(textData.getBorderColor()));
        appearance.setBorderEffect(textData.getBorderEffect());
        appearance.setBorderEffectIntensity(2);
        appearance.setBorderStyle(textData.getBorderStyle());
        appearance.setHCornerRadius(textData.getCornerRadius());
        appearance.setBorderWidth(textData.getBorderWidth());
        appearance.setContents(signOptions.getText() + " " + textData.getContentsDescription());
        appearance.setSubject(textData.getSubject());
        appearance.setTitle(textData.getTitle());
        signOptions.setAppearance(appearance);
        return signOptions;
    }

    public ImagesSignTextOptions signImage(){
        ImagesSignTextOptions signOptions = new ImagesSignTextOptions(textData.getText());
        signOptions.setLeft(signatureData.getLeft());
        signOptions.setTop(signatureData.getTop());
        signOptions.setHeight(signatureData.getImageHeight());
        signOptions.setWidth(signatureData.getImageWidth());
        // setup colors settings
        signOptions.setBackgroundColor(getColor(textData.getBackgroundColor()));
        // setup text color
        signOptions.setForeColor(getColor(textData.getFontColor()));
        // setup Font options
        signOptions.getFont().setBold(textData.getBold());
        signOptions.getFont().setItalic(textData.getItalic());
        signOptions.getFont().setUnderline(textData.getUnderline());
        signOptions.getFont().setFontFamily(textData.getFont());
        signOptions.getFont().setFontSize(textData.getFontSize());
        //type of implementation
        signOptions.setSignatureImplementation(PdfTextSignatureImplementation.Annotation);
        // specify extended appearance options
        PdfTextAnnotationAppearance appearance = new PdfTextAnnotationAppearance();
        appearance.setBorderColor(getColor(textData.getBorderColor()));
        appearance.setBorderEffect(textData.getBorderEffect());
        appearance.setBorderEffectIntensity(2);
        appearance.setBorderStyle(textData.getBorderStyle());
        appearance.setHCornerRadius(textData.getCornerRadius());
        appearance.setBorderWidth(textData.getBorderWidth());
        appearance.setContents(signOptions.getText() + " " + textData.getContentsDescription());
        appearance.setSubject(textData.getSubject());
        appearance.setTitle(textData.getTitle());
        signOptions.setAppearance(appearance);
        return signOptions;
    }

    private Color getColor(String rgbColor){
        String[] colors = rgbColor.split(",");
        int redColor = Integer.parseInt(colors[0].replaceAll("\\D+", ""));
        int greenColor = Integer.parseInt(colors[1].replaceAll("\\D+", ""));
        int blueColor = Integer.parseInt(colors[2].replaceAll("\\D+", ""));
        return new Color(redColor, greenColor, blueColor);
    }
}
