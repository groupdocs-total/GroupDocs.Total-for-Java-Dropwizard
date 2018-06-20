package com.groupdocs.ui.signature.Signer;

import com.groupdocs.signature.domain.enums.DashStyle;
import com.groupdocs.signature.domain.enums.HorizontalAlignment;
import com.groupdocs.signature.domain.enums.VerticalAlignment;
import com.groupdocs.signature.domain.qrcodes.QRCodeTypes;
import com.groupdocs.signature.domain.stamps.StampBackgroundCropType;
import com.groupdocs.signature.domain.stamps.StampLine;
import com.groupdocs.signature.domain.stamps.StampTextRepeatType;
import com.groupdocs.signature.options.qrcodesignature.ImagesQRCodeSignOptions;
import com.groupdocs.signature.options.qrcodesignature.PdfQRCodeSignOptions;
import com.groupdocs.signature.options.stampsignature.*;
import com.groupdocs.ui.signature.domain.wrapper.QrCodeDataWrapper;
import com.groupdocs.ui.signature.domain.wrapper.SignatureDataWrapper;
import com.groupdocs.ui.signature.domain.wrapper.StampDataWrapper;

import java.awt.*;

/**
 * StampSigner
 * Signs documents with the stamp signature
 * @author Aspose Pty Ltd
 */
public class QrCodeSigner {
    private QrCodeDataWrapper qrCodeData;

    public QrCodeSigner(QrCodeDataWrapper qrCodeData){
        this.qrCodeData = qrCodeData;
    }

    public PdfQRCodeSignOptions signPdf(){
        // setup options
        PdfQRCodeSignOptions signOptions = new PdfQRCodeSignOptions(qrCodeData.getText());
        // barcode type
        signOptions.setEncodeType(QRCodeTypes.QR);
        // set border (optionally)
        signOptions.setBorderVisiblity(true);
        signOptions.setBorderColor(getColor(qrCodeData.getBorderColor()));
        signOptions.setBorderWeight(qrCodeData.getBorderWidth());
        signOptions.setBorderDashStyle(qrCodeData.getBorderStyle());
        signOptions.setHorizontalAlignment(HorizontalAlignment.None);
        signOptions.setVerticalAlignment(VerticalAlignment.None);
        signOptions.setTop(0);
        signOptions.setWidth(200);
        signOptions.setHeight(200);
        // set opacity (optionally)
        signOptions.setOpacity(0.5);
        return signOptions;
    }

    public ImagesQRCodeSignOptions signImage(){
        // setup options
        ImagesQRCodeSignOptions signOptions = new ImagesQRCodeSignOptions(qrCodeData.getText());
        // barcode type
        signOptions.setEncodeType(QRCodeTypes.QR);
        // set border (optionally)
        signOptions.setBorderVisiblity(true);
        signOptions.setBorderColor(getColor(qrCodeData.getBorderColor()));
        signOptions.setBorderWeight(qrCodeData.getBorderWidth());
        signOptions.setBorderDashStyle(qrCodeData.getBorderStyle());
        signOptions.setHorizontalAlignment(HorizontalAlignment.None);
        signOptions.setVerticalAlignment(VerticalAlignment.None);
        signOptions.setTop(0);
        signOptions.setWidth(200);
        signOptions.setHeight(200);
        // set opacity (optionally)
        signOptions.setOpacity(0.5);
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
