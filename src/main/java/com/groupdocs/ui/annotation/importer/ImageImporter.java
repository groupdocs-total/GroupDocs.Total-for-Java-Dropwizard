package com.groupdocs.ui.annotation.importer;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.DocumentType;
import com.groupdocs.annotation.handler.AnnotationImageHandler;

import java.io.FileInputStream;

/**
 * ImageImporter
 * Import annotations from the image file
 * @author Aspose Pty Ltd
 */
public class ImageImporter extends Importer {

    public ImageImporter(FileInputStream documentStream, AnnotationImageHandler annotator){
        super(documentStream, annotator);
    }

    public AnnotationInfo[] importAnnotations(){
        AnnotationInfo[] annotations = annotator.importAnnotations(documentStream, DocumentType.Images);
        return annotations;
    }
}
