package com.groupdocs.ui.annotation.importer;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.DocumentType;
import com.groupdocs.annotation.handler.AnnotationImageHandler;

import java.io.FileInputStream;

/**
 * WordImporter
 * Import annotations from the Word file
 * @author Aspose Pty Ltd
 */
public class WordImporter extends Importer {

    public WordImporter(FileInputStream documentStream, AnnotationImageHandler annotator){
        super(documentStream, annotator);
    }

    public AnnotationInfo[] importAnnotations(){
        AnnotationInfo[] annotations = annotator.importAnnotations(documentStream, DocumentType.Words);
        return annotations;
    }
}
