package com.groupdocs.ui.signature.util.directory;

import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.directory.BarcodeDataDirectoryEntity;
import com.groupdocs.ui.signature.entity.directory.CertificateDataDirectoryEntity;
import com.groupdocs.ui.signature.entity.directory.ImageDataDirectoryEntity;
import com.groupdocs.ui.signature.entity.directory.QrCodeDataDirectoryEntity;
import com.groupdocs.ui.signature.entity.directory.StampDataDirectoryEntity;
import com.groupdocs.ui.signature.entity.directory.TextDataDirectoryEntity;

import java.io.File;

/**
 * DataDirectoryUtils
 * Compare and sort file types - folders first
 * @author Aspose Pty Ltd
 */
public class DataDirectoryUtils implements IDirectoryUtils{
    private final String DATA_FOLDER = "/SignatureData";
    private SignatureConfiguration signatureConfiguration;

    private CertificateDataDirectoryEntity certificateDirectory;
    private ImageDataDirectoryEntity imageDirectory;
    private StampDataDirectoryEntity stampDirectory;
    private QrCodeDataDirectoryEntity qrCodeDirectory;
    private BarcodeDataDirectoryEntity barcodeDirectory;
    private TextDataDirectoryEntity textDirectory;

    /**
     * Constructor
     * @param signatureConfiguration
     */
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

    /**
     * Get data directory
     * @return data directory path
     */
    @Override
    public String getPath(){
        return signatureConfiguration.getDataDirectory();
    }

    /**
     * Get certificates directory
     * @return CertificateDataDirectoryEntity
     */
    public CertificateDataDirectoryEntity getCertificateDirectory() {
        return certificateDirectory;
    }

    /**
     * Set certificates directory
     * @param certificateDirectory
     */
    public void setCertificateDirectory(CertificateDataDirectoryEntity certificateDirectory) {
        this.certificateDirectory = certificateDirectory;
    }

    /**
     * Get images directory
     * @return ImageDataDirectoryEntity
     */
    public ImageDataDirectoryEntity getImageDirectory() {
        return imageDirectory;
    }

    /**
     * Set images directory
     * @param imageDirectory
     */
    public void setImageDirectory(ImageDataDirectoryEntity imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    /**
     * Get stamps directory
     * @return StampDataDirectoryEntity
     */
    public StampDataDirectoryEntity getStampDirectory() {
        return stampDirectory;
    }

    /**
     * Set stamps directory
     * @param stampDirectory
     */
    public void setStampDirectory(StampDataDirectoryEntity stampDirectory) {
        this.stampDirectory = stampDirectory;
    }

    /**
     * Get Qr-Code directory
     * @return QrCodeDataDirectoryEntity
     */
    public QrCodeDataDirectoryEntity getQrCodeDirectory() {
        return qrCodeDirectory;
    }

    /**
     * Set Qr-Code directory
     * @param qrCodeDirectory
     */
    public void setQrCodeDirectory(QrCodeDataDirectoryEntity qrCodeDirectory) {
        this.qrCodeDirectory = qrCodeDirectory;
    }

    /**
     * Get BarCode directory
     * @return BarcodeDataDirectoryEntity
     */
    public BarcodeDataDirectoryEntity getBarcodeDirectory() {
        return barcodeDirectory;
    }

    /**
     * Set BarCode directory
     * @param barcodeDirectory
     */
    public void setBarcodeDirectory(BarcodeDataDirectoryEntity barcodeDirectory) {
        this.barcodeDirectory = barcodeDirectory;
    }

    /**
     * Get text signature directory
     * @return TextDataDirectoryEntity
     */
    public TextDataDirectoryEntity getTextDirectory() {
        return textDirectory;
    }

    /**
     * Set text signature directory
     * @param textDirectory
     */
    public void setTextDirectory(TextDataDirectoryEntity textDirectory) {
        this.textDirectory = textDirectory;
    }
}
