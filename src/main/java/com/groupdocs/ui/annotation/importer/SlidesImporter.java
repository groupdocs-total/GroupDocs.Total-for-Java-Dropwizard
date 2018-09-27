package com.groupdocs.ui.annotation.importer;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.DocumentType;
import com.groupdocs.annotation.handler.AnnotationImageHandler;

import java.io.FileInputStream;

/**
 * SlidesImporter
 * Import annotations from the PowerPoint file
 * @author Aspose Pty Ltd
 */
public class SlidesImporter extends Importer {

    public SlidesImporter(FileInputStream documentStream, AnnotationImageHandler annotator){
        super(documentStream, annotator);
    }

    public AnnotationInfo[] importAnnotations(){
        AnnotationInfo[] annotations = annotator.importAnnotations(documentStream, DocumentType.Slides);
        return annotations;
    }
}
