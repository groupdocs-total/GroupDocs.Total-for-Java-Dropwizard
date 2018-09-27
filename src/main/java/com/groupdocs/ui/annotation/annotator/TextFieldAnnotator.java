package com.groupdocs.ui.annotation.annotator;

import com.groupdocs.annotation.domain.*;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.NotSupportedException;
import java.text.ParseException;

/**
 * TextAnnotator
 * Annotates documents with the text annotation
 * @author Aspose Pty Ltd
 */
public class TextFieldAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public TextFieldAnnotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
        super(annotationData, documentInfo);
    }

    /**
     * Add area annnotation into the Word document
     */
    @Override
    public AnnotationInfo annotateWord() throws ParseException {
        // init possible types of annotations
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(annotationData.getFont());
        textFieldAnnotation.setFontSize(annotationData.getFontSize());
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);
        return textFieldAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
    
     */
    @Override
    public AnnotationInfo annotatePdf() throws ParseException {
        // init possible types of annotations
        // Text field annotation
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(StringUtils.capitalize(annotationData.getFont()));
        textFieldAnnotation.setFontSize(annotationData.getFontSize());
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);
        return textFieldAnnotation;
    }

    /**
     * Add area annnotation into the Excel document
    */
    @Override
    public AnnotationInfo annotateCells() throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * Add area annnotation into the Power Point document
     */
    @Override
    public AnnotationInfo annotateSlides() throws ParseException {
        // init possible types of annotations
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(annotationData.getFont());
        textFieldAnnotation.setFontSize(annotationData.getFontSize());
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);
        return textFieldAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateImage() throws ParseException {
        // init possible types of annotations
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(annotationData.getFont());
        textFieldAnnotation.setFontColor(-15988609);
        textFieldAnnotation.setFontSize(annotationData.getFontSize());
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);
        return textFieldAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram() throws ParseException {
        // init possible types of annotations
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(annotationData.getFont());
        textFieldAnnotation.setFontColor(-15988609);
        textFieldAnnotation.setFontSize(annotationData.getFontSize());
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);
        return textFieldAnnotation;
    }
}