package com.groupdocs.ui.annotation.util.directory;

import com.groupdocs.ui.annotation.config.AnnotationConfiguration;

import java.io.File;

/**
 * OutputDirectoryUtils
 * Compare and sort file types - folders first
 * @author Aspose Pty Ltd
 */
public class OutputDirectoryUtils implements IDirectoryUtils {
    private static final String OUTPUT_FOLDER = File.separator + "Annotated";
    private AnnotationConfiguration annotationConfiguration;

    /**
     * Constructor
     * @param annotationConfiguration
     */
    public OutputDirectoryUtils(AnnotationConfiguration annotationConfiguration){
        this.annotationConfiguration = annotationConfiguration;

        // create output directories
        if(annotationConfiguration.getOutputDirectory() == null || annotationConfiguration.getOutputDirectory().isEmpty()){
            annotationConfiguration.setOutputDirectory(annotationConfiguration.getFilesDirectory() + OUTPUT_FOLDER);
        }
        if(!new File(annotationConfiguration.getOutputDirectory()).exists()) {
            new File(annotationConfiguration.getOutputDirectory()).mkdirs();
        }
    }

    /**
     * Get Output directory path
     * @return path for folder where to save signed documents
     */
    @Override
    public String getPath() {
        return annotationConfiguration.getOutputDirectory();
    }
}
