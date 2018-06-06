package com.groupdocs.ui.signature.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;

/**
 * SignatureConfiguration
 *
 * @author Aspose Pty Ltd
 */
public class SignatureConfiguration extends Configuration{

    @Valid
    @JsonProperty
    private String filesDirectory;

    @Valid
    @JsonProperty
    private String outputPath;

    @Valid
    @JsonProperty
    private String outputImagePath;

    @Valid
    @JsonProperty
    private String certificatePath;

    @Valid
    @JsonProperty
    private String imagePath;

    @Valid
    @JsonProperty
    private boolean  textSignature;

    @Valid
    @JsonProperty
    private boolean imageSignature;

    @Valid
    @JsonProperty
    private boolean digitalSignature;

    @Valid
    @JsonProperty
    private boolean qrCodeSignature;

    @Valid
    @JsonProperty
    private boolean barCodeSignature;

    @Valid
    @JsonProperty
    private boolean stampSignature;

    @Valid
    @JsonProperty
    private boolean  downloadOriginal;

    @Valid
    @JsonProperty
    private boolean downloadSigned;

    public boolean getTextSignature() {
        return textSignature;
    }

    public void setTextSignature(boolean textSignature) {
        this.textSignature = textSignature;
    }

    public boolean getImageSignature() {
        return imageSignature;
    }

    public void setImageSignature(boolean imageSignature) {
        this.imageSignature = imageSignature;
    }

    public boolean getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(boolean digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public boolean getQrCodeSignature() {
        return qrCodeSignature;
    }

    public void setQrCodeSignature(boolean qrCodeSignature) {
        this.qrCodeSignature = qrCodeSignature;
    }

    public boolean getBarCodeSignature() {
        return barCodeSignature;
    }

    public void setBarCodeSignature(boolean barCodeSignature) {
        this.barCodeSignature = barCodeSignature;
    }

    public boolean getStampSignature() {
        return stampSignature;
    }

    public void setStampSignature(boolean stampSignature) {
        this.stampSignature = stampSignature;
    }

    public boolean getDownloadOriginal() {
        return downloadOriginal;
    }

    public void setDownloadOriginal(boolean downloadOriginal) {
        this.downloadOriginal = downloadOriginal;
    }

    public boolean getDownloadSigned() {
        return downloadSigned;
    }

    public void setDownloadSigned(boolean downloadSigned) {
            this.downloadSigned = downloadSigned;
        }

    public String getFilesDirectory() {
        return filesDirectory;
    }

    public void setFilesDirectory(String filesDirectory) {
        this.filesDirectory = filesDirectory;
    }

    public String getOutputPath() { return outputPath; }

    public void setOutputPath(String outputPath) { this.outputPath = outputPath; }

    public String getOutputImagePath() { return outputImagePath; }

    public void setOutputImagePath(String outputImagePath) { this.outputImagePath = outputImagePath; }

    public String getCertificatePath() { return certificatePath; }

    public void setCertificatePath(String certificatePath) { this.certificatePath = certificatePath; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}


