package com.groupdocs.ui.signature.signer;

import com.groupdocs.signature.domain.enums.HorizontalAlignment;
import com.groupdocs.signature.domain.enums.VerticalAlignment;
import com.groupdocs.signature.domain.qrcodes.QRCodeTypes;
import com.groupdocs.signature.options.qrcodesignature.SlidesQRCodeSignOptions;
import com.groupdocs.signature.options.qrcodesignature.CellsQRCodeSignOptions;
import com.groupdocs.signature.options.qrcodesignature.WordsQRCodeSignOptions;
import com.groupdocs.signature.options.qrcodesignature.PdfQRCodeSignOptions;
import com.groupdocs.signature.options.qrcodesignature.ImagesQRCodeSignOptions;
import com.groupdocs.ui.signature.entity.xml.OpticalXmlEntity;
import com.groupdocs.ui.signature.domain.wrapper.SignatureDataWrapper;

import java.awt.Color;
/**
 * StampSigner
 * Signs documents with the stamp signature
 * @author Aspose Pty Ltd
 */
public class QrCodeSigner extends Signer{
    private OpticalXmlEntity qrCodeData;


    public QrCodeSigner(OpticalXmlEntity qrCodeData, SignatureDataWrapper signatureData){
        super(signatureData);
        this.qrCodeData = qrCodeData;
    }

    @Override
    public PdfQRCodeSignOptions signPdf(){
        // setup options
        PdfQRCodeSignOptions signOptions = new PdfQRCodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(QRCodeTypes.QR);
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

    @Override
    public ImagesQRCodeSignOptions signImage(){
        // setup options
        ImagesQRCodeSignOptions signOptions = new ImagesQRCodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(QRCodeTypes.QR);
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

    @Override
    public WordsQRCodeSignOptions signWord(){
        // setup options
        WordsQRCodeSignOptions signOptions = new WordsQRCodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(QRCodeTypes.QR);
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

    @Override
    public CellsQRCodeSignOptions signCells(){
        // setup options
        CellsQRCodeSignOptions signOptions = new CellsQRCodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(QRCodeTypes.QR);
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

    @Override
    public SlidesQRCodeSignOptions signSlides(){
        // setup options
        SlidesQRCodeSignOptions signOptions = new SlidesQRCodeSignOptions(qrCodeData.getText());
        signOptions.setEncodeType(QRCodeTypes.QR);
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

}
