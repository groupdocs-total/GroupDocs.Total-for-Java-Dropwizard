package com.groupdocs.ui.annotation.util.directory;

import com.groupdocs.ui.annotation.config.AnnotationConfiguration;

import java.io.File;

/**
 * FilesDirectoryUtils
 * Compare and sort file types - folders first
 * @author Aspose Pty Ltd
 */
public class FilesDirectoryUtils implements IDirectoryUtils {
    private AnnotationConfiguration annotationConfiguration;

    /**
     * Constructor
     * @param annotationConfiguration
     */
    public FilesDirectoryUtils(AnnotationConfiguration annotationConfiguration){
        this.annotationConfiguration = annotationConfiguration;

        // set files directory
        if(!new File(annotationConfiguration.getFilesDirectory()).exists()) {
            annotationConfiguration.setFilesDirectory(new File("").getAbsolutePath() + annotationConfiguration.getFilesDirectory());
        }
    }

    /**
     * Get path for files directory
     * @return path of the files directory
     */
    @Override
    public String getPath() {
        return annotationConfiguration.getFilesDirectory();
    }

}
