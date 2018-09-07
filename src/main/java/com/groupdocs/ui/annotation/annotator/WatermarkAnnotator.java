package com.groupdocs.ui.annotation.annotator;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.AnnotationType;
import com.groupdocs.annotation.domain.Point;
import com.groupdocs.annotation.domain.Rectangle;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.annotation.entity.web.CommentsEntity;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.NotSupportedException;
import java.text.ParseException;

/**
 * TextAnnotator
 * Annotates documents with the text annotation
 * @author Aspose Pty Ltd
 */
public class WatermarkAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public WatermarkAnnotator(AnnotationDataEntity annotationData){
        super(annotationData);
    }

    /**
     * Add area annnotation into the Word document
     * @param info
     * @param comment
     */
    @Override
    public AnnotationInfo annotateWord(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * Add area annnotation into the pdf document
     * @param info
     */
    @Override
    public AnnotationInfo annotatePdf(DocumentInfoContainer info) throws ParseException {
        // init possible types of annotations
        // Text field annotation
        AnnotationInfo watermarkAnnotation = new AnnotationInfo();
        watermarkAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        watermarkAnnotation.setFieldText(annotationData.getText());
        watermarkAnnotation.setFontFamily(StringUtils.capitalize(annotationData.getFont()));
        watermarkAnnotation.setFontSize(annotationData.getFontSize());
        watermarkAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        watermarkAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        watermarkAnnotation.setType(AnnotationType.Watermark);
        return watermarkAnnotation;
    }

    /**
     * Add area annnotation into the Excel document
     * @param info
     * @param comment
     */
    @Override
    public AnnotationInfo annotateCells(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * Add area annnotation into the Power Point document
     * @param info
     */
    @Override
    public AnnotationInfo annotateSlides(DocumentInfoContainer info) throws ParseException {
        // init possible types of annotations
        AnnotationInfo watermarkAnnotation = new AnnotationInfo();
        watermarkAnnotation.setFieldText(annotationData.getText());
        watermarkAnnotation.setFontFamily(annotationData.getFont());
        watermarkAnnotation.setFontSize(annotationData.getFontSize());
        watermarkAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        watermarkAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        watermarkAnnotation.setType(AnnotationType.Watermark);

        return watermarkAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        // init possible types of annotations
        AnnotationInfo watermarkAnnotation = new AnnotationInfo();
        watermarkAnnotation.setFieldText(annotationData.getText());
        watermarkAnnotation.setFontFamily(annotationData.getFont());
        watermarkAnnotation.setFontColor(15988609);
        watermarkAnnotation.setFontSize(annotationData.getFontSize());
        watermarkAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        watermarkAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        watermarkAnnotation.setType(AnnotationType.Watermark);

        return watermarkAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }
}