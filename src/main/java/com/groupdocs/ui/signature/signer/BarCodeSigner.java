package com.groupdocs.ui.signature.signer;

import com.groupdocs.signature.domain.barcodes.BarcodeTypes;
import com.groupdocs.signature.domain.enums.HorizontalAlignment;
import com.groupdocs.signature.domain.enums.VerticalAlignment;
import com.groupdocs.signature.options.barcodesignature.CellsBarcodeSignOptions;
import com.groupdocs.signature.options.barcodesignature.WordsBarcodeSignOptions;
import com.groupdocs.signature.options.barcodesignature.ImagesBarcodeSignOptions;
import com.groupdocs.signature.options.barcodesignature.PdfBarcodeSignOptions;
import com.groupdocs.signature.options.barcodesignature.SlidesBarcodeSignOptions;
import com.groupdocs.ui.signature.entity.xml.OpticalXmlEntity;
import com.groupdocs.ui.signature.domain.wrapper.SignatureDataWrapper;

import java.awt.Color;

/**
 * StampSigner
 * Signs documents with the stamp signature
 * @author Aspose Pty Ltd
 */
public class BarCodeSigner {
    private OpticalXmlEntity qrCodeData;
    private SignatureDataWrapper signatureData;

    public BarCodeSigner(OpticalXmlEntity qrCodeData, SignatureDataWrapper signatureData){
        this.qrCodeData = qrCodeData;
        this.signatureData = signatureData;
    }

    public PdfBarcodeSignOptions signPdf(){
        // setup options
        PdfBarcodeSignOptions signOptions = new PdfBarcodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(BarcodeTypes.CODE_39_STANDARD);
        signOptions.setHorizontalAlignment(HorizontalAlignment.None);
        signOptions.setVerticalAlignment(VerticalAlignment.None);
        signOptions.setWidth(signatureData.getImageWidth());
        signOptions.setHeight(signatureData.getImageHeight());
        signOptions.setTop(signatureData.getTop());
        signOptions.setLeft(signatureData.getLeft());
        signOptions.setDocumentPageNumber(signatureData.getPageNumber());
        signOptions.setRotationAngle(signatureData.getAngle());
        if(qrCodeData.getBorderWidth() != 0){
            signOptions.setBorderVisiblity(true);
            signOptions.setBorderColor(getColor(qrCodeData.getBorderColor()));
            signOptions.setBorderWeight(qrCodeData.getBorderWidth());
            signOptions.setBorderDashStyle(qrCodeData.getBorderStyle());
        }
        return signOptions;
    }

    public ImagesBarcodeSignOptions signImage(){
        // setup options
        ImagesBarcodeSignOptions signOptions = new ImagesBarcodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(BarcodeTypes.CODE_39_STANDARD);
        signOptions.setHorizontalAlignment(HorizontalAlignment.None);
        signOptions.setVerticalAlignment(VerticalAlignment.None);
        signOptions.setWidth(signatureData.getImageWidth());
        signOptions.setHeight(signatureData.getImageHeight());
        signOptions.setTop(signatureData.getTop());
        signOptions.setLeft(signatureData.getLeft());
        if(signatureData.getAngle() != 0) {
            signOptions.setRotationAngle(signatureData.getAngle());
        }
        if(qrCodeData.getBorderWidth() != 0){
            signOptions.setBorderVisiblity(true);
            signOptions.setBorderColor(getColor(qrCodeData.getBorderColor()));
            signOptions.setBorderWeight(qrCodeData.getBorderWidth());
            signOptions.setBorderDashStyle(qrCodeData.getBorderStyle());
        }
        return signOptions;
    }

    public WordsBarcodeSignOptions signWord(){
        // setup options
        WordsBarcodeSignOptions signOptions = new WordsBarcodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(BarcodeTypes.CODE_39_STANDARD);
        signOptions.setHorizontalAlignment(HorizontalAlignment.None);
        signOptions.setVerticalAlignment(VerticalAlignment.None);
        signOptions.setWidth(signatureData.getImageWidth());
        signOptions.setHeight(signatureData.getImageHeight());
        signOptions.setTop(signatureData.getTop());
        signOptions.setLeft(signatureData.getLeft());
        signOptions.setDocumentPageNumber(signatureData.getPageNumber());
        signOptions.setRotationAngle(signatureData.getAngle());
        if(qrCodeData.getBorderWidth() != 0){
            signOptions.setBorderVisiblity(true);
            signOptions.setBorderColor(getColor(qrCodeData.getBorderColor()));
            signOptions.setBorderWeight(qrCodeData.getBorderWidth());
            signOptions.setBorderDashStyle(qrCodeData.getBorderStyle());
        }
        return signOptions;
    }

    public CellsBarcodeSignOptions signCells(){
        // setup options
        CellsBarcodeSignOptions signOptions = new CellsBarcodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(BarcodeTypes.CODE_39_STANDARD);
        signOptions.setHorizontalAlignment(HorizontalAlignment.None);
        signOptions.setVerticalAlignment(VerticalAlignment.None);
        signOptions.setWidth(signatureData.getImageWidth());
        signOptions.setHeight(signatureData.getImageHeight());
        signOptions.setTop(signatureData.getTop());
        signOptions.setLeft(signatureData.getLeft());
        signOptions.setDocumentPageNumber(signatureData.getPageNumber());
        signOptions.setRotationAngle(signatureData.getAngle());
        if(qrCodeData.getBorderWidth() != 0){
            signOptions.setBorderVisiblity(true);
            signOptions.setBorderColor(getColor(qrCodeData.getBorderColor()));
            signOptions.setBorderWeight(qrCodeData.getBorderWidth());
            signOptions.setBorderDashStyle(qrCodeData.getBorderStyle());
        }
        return signOptions;
    }

    public SlidesBarcodeSignOptions signSlides(){
        // setup options
        SlidesBarcodeSignOptions signOptions = new SlidesBarcodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(BarcodeTypes.CODE_39_STANDARD);
        signOptions.setHorizontalAlignment(HorizontalAlignment.None);
        signOptions.setVerticalAlignment(VerticalAlignment.None);
        signOptions.setWidth(signatureData.getImageWidth());
        signOptions.setHeight(signatureData.getImageHeight());
        signOptions.setTop(signatureData.getTop());
        signOptions.setLeft(signatureData.getLeft());
        signOptions.setDocumentPageNumber(signatureData.getPageNumber());
        signOptions.setRotationAngle(signatureData.getAngle());
        if(qrCodeData.getBorderWidth() != 0){
            signOptions.setBorderVisiblity(true);
            signOptions.setBorderColor(getColor(qrCodeData.getBorderColor()));
            signOptions.setBorderWeight(qrCodeData.getBorderWidth());
            signOptions.setBorderDashStyle(qrCodeData.getBorderStyle());
        }
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
