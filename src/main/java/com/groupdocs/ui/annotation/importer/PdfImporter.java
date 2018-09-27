package com.groupdocs.ui.annotation.importer;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.DocumentType;
import com.groupdocs.annotation.handler.AnnotationImageHandler;

import java.io.FileInputStream;

/**
 * PdfImporter
 * Import annotations from the PDF file
 * @author Aspose Pty Ltd
 */
public class PdfImporter extends Importer {

    public PdfImporter(FileInputStream documentStream, AnnotationImageHandler annotator){
        super(documentStream, annotator);
    }

    public AnnotationInfo[] importAnnotations(){
        AnnotationInfo[] annotations = annotator.importAnnotations(documentStream, DocumentType.Pdf);
        return annotations;
    }
}
