package com.groupdocs.ui.signature.service;

import com.groupdocs.signature.config.SignatureConfig;
import com.groupdocs.signature.handler.SignatureHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.FileSystems;

import static com.groupdocs.ui.signature.util.directory.SignatureDirectory.*;

public class SignatureHandlerFactory {

    public static SignatureHandler instance;
    public static SignatureHandler streamInstance;

    /**
     * Create instance of SignatureHandler
     *
     * @param filesDirectory
     * @param dataDirectory
     * @return
     */
    public synchronized static SignatureHandler createHandler(String filesDirectory, String dataDirectory) {
        if (instance == null) {
            String directory = StringUtils.isEmpty(dataDirectory) ? filesDirectory : dataDirectory;
            // create directories
            createDirectories(dataDirectory);

            // create signature application configuration
            SignatureConfig config = new SignatureConfig();
            config.setStoragePath(filesDirectory);
            config.setCertificatesPath(getFullDataPath(directory, CERTIFICATE_DATA_DIRECTORY.getPath()));
            config.setImagesPath(getFullDataPath(directory, IMAGE_DATA_DIRECTORY.getPath()));

            instance = new SignatureHandler(config);
        }
        return instance;
    }

    /**
     * Create instance of SignatureHandler for streams
     *
     * @return
     */
    public synchronized static SignatureHandler createStreamHandler() {
        if (streamInstance == null) {
            SignatureConfig config = new SignatureConfig();
            config.setOutputPath(FileSystems.getDefault().getPath("").toAbsolutePath().toString());
            SignatureHandler<OutputStream> streamSignatureHandler = new SignatureHandler<>(config);
            streamInstance = streamSignatureHandler;
        }
        return streamInstance;
    }

    public static String getFullDataPath(String dataDirectory, String partPath) {
        return String.format("%s%s", dataDirectory, partPath);
    }

    public static void createDirectories(String dataDirectory) {
        new File(getFullDataPath(dataDirectory, CERTIFICATE_DATA_DIRECTORY.getPath())).mkdirs();
        new File(getFullDataPath(dataDirectory, IMAGE_DATA_DIRECTORY.getPath())).mkdirs();
        new File(getFullDataPath(dataDirectory, IMAGE_UPLOADED_DATA_DIRECTORY.getPath())).mkdirs();

        new File(getFullDataPath(dataDirectory, STAMP_DATA_DIRECTORY.getXMLPath())).mkdirs();
        new File(getFullDataPath(dataDirectory, STAMP_DATA_DIRECTORY.getPreviewPath())).mkdirs();

        new File(getFullDataPath(dataDirectory, QRCODE_DATA_DIRECTORY.getXMLPath())).mkdirs();
        new File(getFullDataPath(dataDirectory, QRCODE_DATA_DIRECTORY.getPreviewPath())).mkdirs();

        new File(getFullDataPath(dataDirectory, BARCODE_DATA_DIRECTORY.getXMLPath())).mkdirs();
        new File(getFullDataPath(dataDirectory, BARCODE_DATA_DIRECTORY.getPreviewPath())).mkdirs();

        new File(getFullDataPath(dataDirectory, TEXT_DATA_DIRECTORY.getXMLPath())).mkdirs();
    }
}
