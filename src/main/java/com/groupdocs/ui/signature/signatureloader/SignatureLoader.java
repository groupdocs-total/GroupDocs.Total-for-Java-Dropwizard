package com.groupdocs.ui.signature.signatureloader;

import com.google.common.collect.Ordering;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.util.comparator.FileNameComparator;
import com.groupdocs.ui.common.util.comparator.FileTypeComparator;
import com.groupdocs.ui.signature.entity.web.SignatureFileDescriptionEntity;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * SignatureLoader
 * Loads signature files from the storage
 * @author Aspose Pty Ltd
 */
public class SignatureLoader {
    private String path;
    private GlobalConfiguration globalConfiguration;

    /**
     * Constructor
     * @param path signatures root directory
     * @param globalConfiguration global configuration object
     */
    public SignatureLoader(String path, GlobalConfiguration globalConfiguration){
        this.path = path;
        this.globalConfiguration = globalConfiguration;
    }

    /**
     * Load image signatures
     * @return List<SignatureFileDescriptionEntity>
     * @throws IOException
     */
    public List<SignatureFileDescriptionEntity> loadImageSignatures() throws IOException {
        File directory = new File(path);
        List<SignatureFileDescriptionEntity> fileList = new ArrayList<>();
        List<File> filesList = Arrays.asList(directory.listFiles());
        try {
            // sort list of files and folders
            filesList = Ordering.from(FileTypeComparator.instance).compound(FileNameComparator.instance).sortedCopy(filesList);
            for (File file : filesList) {
                // check if current file/folder is hidden
                if (file.isHidden() || file.toPath().equals(new File(globalConfiguration.getSignature().getDataDirectory()).toPath())) {
                    // ignore current file and skip to next one
                    continue;
                } else {
                    SignatureFileDescriptionEntity fileDescription = getSignatureFileDescriptionEntity(file, true);
                    // add object to array list
                    fileList.add(fileDescription);
                }
            }
            return fileList;
        } catch (Exception ex){
            throw ex;
        }
    }

    /**
     * Load digital signatures or documents for signing
     * @return List<SignatureFileDescriptionEntity>
     * @throws IOException
     */
    public List<SignatureFileDescriptionEntity> loadFiles() throws IOException {
        File directory = new File(path);
        List<SignatureFileDescriptionEntity> fileList = new ArrayList<>();
        List<File> filesList = Arrays.asList(directory.listFiles());
        try {
            // sort list of files and folders
            filesList = Ordering.from(FileTypeComparator.instance).compound(FileNameComparator.instance).sortedCopy(filesList);
            for (File file : filesList) {
                // check if current file/folder is hidden
                if (file.isHidden() || file.toPath().equals(new File(globalConfiguration.getSignature().getDataDirectory()).toPath())) {
                    // ignore current file and skip to next one
                    continue;
                } else {
                    SignatureFileDescriptionEntity fileDescription = getSignatureFileDescriptionEntity(file, false);
                    // add object to array list
                    fileList.add(fileDescription);
                }
            }
            return fileList;
        } catch (Exception ex){
            throw ex;
        }
    }

    /**
     * Load stamp signatures
     * @param previewFolder
     * @param xmlFolder
     * @return List<SignatureFileDescriptionEntity>
     * @throws IOException
     */
    public List<SignatureFileDescriptionEntity> loadStampSignatures(String previewFolder, String xmlFolder) throws IOException {
        String imagesPath = path + previewFolder;
        String xmlPath = path + xmlFolder;
        File images = new File(imagesPath);
        List<SignatureFileDescriptionEntity> fileList = new ArrayList<>();
        try {
            if(images.listFiles() != null) {
                List<File> imageFiles = Arrays.asList(images.listFiles());
                File xmls = new File(xmlPath);
                List<File> xmlFiles = Arrays.asList(xmls.listFiles());
                List<File> filesList = new ArrayList<>();
                for (File image : imageFiles) {
                    for (File xmlFile : xmlFiles) {
                        if (FilenameUtils.removeExtension(xmlFile.getName()).equals(FilenameUtils.removeExtension(image.getName()))) {
                            filesList.add(image);
                        }
                    }
                }
                // sort list of files and folders
                filesList = Ordering.from(FileTypeComparator.instance).compound(FileNameComparator.instance).sortedCopy(filesList);
                for (File file : filesList) {
                    // check if current file/folder is hidden
                    if (file.isHidden() || file.toPath().equals(new File(globalConfiguration.getSignature().getDataDirectory()).toPath())) {
                        // ignore current file and skip to next one
                        continue;
                    } else {
                        SignatureFileDescriptionEntity fileDescription = getSignatureFileDescriptionEntity(file, true);
                        // add object to array list
                        fileList.add(fileDescription);
                    }
                }
            }
            return fileList;
        } catch (Exception ex){
            throw ex;
        }
    }

    /**
     * Create file description
     *
     * @param file file
     * @param withImage set image
     * @return signature file description
     * @throws IOException
     */
    private SignatureFileDescriptionEntity getSignatureFileDescriptionEntity(File file, boolean withImage) throws IOException {
        SignatureFileDescriptionEntity fileDescription = new SignatureFileDescriptionEntity();
        fileDescription.setGuid(file.getAbsolutePath());
        fileDescription.setName(file.getName());
        // set is directory true/false
        fileDescription.setDirectory(file.isDirectory());
        // set file size
        fileDescription.setSize(file.length());
        if (withImage) {
            // get image Base64 encoded String
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            fileDescription.setImage(Base64.getEncoder().encodeToString(bytes));
        }
        return fileDescription;
    }
}
