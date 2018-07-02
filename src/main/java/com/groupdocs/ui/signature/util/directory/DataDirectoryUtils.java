package com.groupdocs.ui.signature.util.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.*;

import java.io.File;

public class DataDirectoryUtils implements IDirectoryUtils{
    private final String DATA_FOLDER = "/SignatureData";
    private SignatureConfiguration signatureConfiguration;

    private CertificateDataDirectoryEntity certificateDirectory;
    private ImageDataDirectoryEntity imageDirectory;
    private StampDataDirectoryEntity stampDirectory;
    private QrCodeDataDirectoryEntity qrCodeDirectory;
    private BarcodeDataDirectoryEntity barcodeDirectory;
    private TextDataDirectoryEntity textDirectory;

    public DataDirectoryUtils(SignatureConfiguration signatureConfiguration) {
        this.signatureConfiguration = signatureConfiguration;

        // check if data directory was set, if not set new directory
        if (signatureConfiguration.getDataDirectory() == null || signatureConfiguration.getDataDirectory().isEmpty()) {
            signatureConfiguration.setDataDirectory(signatureConfiguration.getFilesDirectory() + DATA_FOLDER);
        }

        // create directory objects
        barcodeDirectory = new BarcodeDataDirectoryEntity(signatureConfiguration);
        certificateDirectory = new CertificateDataDirectoryEntity(signatureConfiguration);
        imageDirectory = new ImageDataDirectoryEntity(signatureConfiguration);
        stampDirectory = new StampDataDirectoryEntity(signatureConfiguration);
        qrCodeDirectory = new QrCodeDataDirectoryEntity(signatureConfiguration);
        barcodeDirectory = new BarcodeDataDirectoryEntity(signatureConfiguration);
        textDirectory = new TextDataDirectoryEntity(signatureConfiguration);

        // create directories
        new File(certificateDirectory.getPath()).mkdirs();
        new File(imageDirectory.getPath()).mkdirs();

        new File(stampDirectory.getXmlPath()).mkdirs();
        new File(stampDirectory.getPreviewPath()).mkdirs();

        new File(qrCodeDirectory.getXmlPath()).mkdirs();
        new File(qrCodeDirectory.getPreviewPath()).mkdirs();

        new File(barcodeDirectory.getXmlPath()).mkdirs();
        new File(barcodeDirectory.getPreviewPath()).mkdirs();

        new File(textDirectory.getXmlPath()).mkdirs();
        new File(textDirectory.getPreviewPath()).mkdirs();
    }

    @Override
    public String getPath(){
        return signatureConfiguration.getDataDirectory();
    }

    public CertificateDataDirectoryEntity getCertificateDirectory() {
        return certificateDirectory;
    }

    public void setCertificateDirectory(CertificateDataDirectoryEntity certificateDirectory) {
        this.certificateDirectory = certificateDirectory;
    }

    public ImageDataDirectoryEntity getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(ImageDataDirectoryEntity imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public StampDataDirectoryEntity getStampDirectory() {
        return stampDirectory;
    }

    public void setStampDirectory(StampDataDirectoryEntity stampDirectory) {
        this.stampDirectory = stampDirectory;
    }

    public QrCodeDataDirectoryEntity getQrCodeDirectory() {
        return qrCodeDirectory;
    }

    public void setQrCodeDirectory(QrCodeDataDirectoryEntity qrCodeDirectory) {
        this.qrCodeDirectory = qrCodeDirectory;
    }

    public BarcodeDataDirectoryEntity getBarcodeDirectory() {
        return barcodeDirectory;
    }

    public void setBarcodeDirectory(BarcodeDataDirectoryEntity barcodeDirectory) {
        this.barcodeDirectory = barcodeDirectory;
    }

    public TextDataDirectoryEntity getTextDirectory() {
        return textDirectory;
    }

    public void setTextDirectory(TextDataDirectoryEntity textDirectory) {
        this.textDirectory = textDirectory;
    }
}
