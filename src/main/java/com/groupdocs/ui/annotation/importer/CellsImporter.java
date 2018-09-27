package com.groupdocs.ui.annotation.importer;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.DocumentType;
import com.groupdocs.annotation.handler.AnnotationImageHandler;

import java.io.FileInputStream;

/**
 * CellsImporter
 * Import annotations from the Excel document
 * @author Aspose Pty Ltd
 */
public class CellsImporter extends Importer {

    public CellsImporter(FileInputStream documentStream, AnnotationImageHandler annotator){
        super(documentStream, annotator);
    }

    public AnnotationInfo[] importAnnotations(){
        AnnotationInfo[] annotations = annotator.importAnnotations(documentStream, DocumentType.Cells);
        return annotations;
    }
}
