package com.groupdocs.ui.annotation.importer;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.handler.AnnotationImageHandler;

import java.io.FileInputStream;

/**
 * Annotator
 * Abstract class contains general description for the annotating functionality
 * @author Aspose Pty Ltd
 */

/**
 * Importer
 * Abstract class
 * @author Aspose Pty Ltd
 */
public abstract class Importer {
    protected FileInputStream  documentStream;
    protected AnnotationImageHandler annotator;

    /**
     * Constructor
     * @param documentStream
     */
    public Importer(FileInputStream documentStream, AnnotationImageHandler annotator){
        this.documentStream = documentStream;
        this.annotator = annotator;
    }

    /**
     *
     * @return AnnotationInfo[]     
     */
    public abstract AnnotationInfo[] importAnnotations();

}
