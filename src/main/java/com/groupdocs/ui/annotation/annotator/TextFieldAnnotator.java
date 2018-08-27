package com.groupdocs.ui.annotation.annotator;

import com.groupdocs.annotation.domain.*;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.annotation.entity.web.CommentsEntity;

import javax.ws.rs.NotSupportedException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

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
    public TextFieldAnnotator(AnnotationDataEntity annotationData){
        super(annotationData);
    }

    /**
     * Add area annnotation into the Word document
     * @param info
     * @param comment
     */
    @Override
    public AnnotationInfo annotateWord(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
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
     * @param info
     */
    @Override
    public AnnotationInfo annotatePdf(DocumentInfoContainer info) throws ParseException {
        // init possible types of annotations
        // Text field annotation
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(annotationData.getFont().toUpperCase());
        textFieldAnnotation.setFontSize(annotationData.getFontSize());
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);
        return textFieldAnnotation;
    }

    /**
     * Add area annnotation into the Excel document
     * @param info
     * @param comment
     */
    @Override
    public AnnotationInfo annotateCells(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        throw new NotSupportedException("This file type is not supported");
    }

    /**
     * Add area annnotation into the Power Point document
     * @param info
     */
    @Override
    public AnnotationInfo annotateSlides(DocumentInfoContainer info) throws ParseException {
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
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        // init possible types of annotations
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(annotationData.getFont());
        textFieldAnnotation.setFontColor(-15988609);
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);

        return textFieldAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        // init possible types of annotations
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setFieldText(annotationData.getText());
        textFieldAnnotation.setFontFamily(annotationData.getFont());
        textFieldAnnotation.setFontColor(-15988609);
        textFieldAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        textFieldAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        textFieldAnnotation.setType(AnnotationType.TextField);

        return textFieldAnnotation;
    }
}