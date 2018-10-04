package com.groupdocs.ui.annotation.annotator;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.AnnotationType;
import com.groupdocs.annotation.domain.PageData;
import com.groupdocs.annotation.domain.Point;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;

import java.text.ParseException;

/**
 * TextAnnotator
 * Annotates documents with the text annotation
 *
 * @author Aspose Pty Ltd
 */
public class TexUnderlineAnnotator extends AbstractSvgAnnotator {

    public TexUnderlineAnnotator(AnnotationDataEntity annotationData, PageData pageData) {
        super(annotationData, pageData);
    }

    @Override
    public AnnotationInfo annotateWord() throws ParseException {
        setFixTop(true);
        // init possible types of annotations
        AnnotationInfo underlineAnnotation = initAnnotationInfo();
        // set line color
        underlineAnnotation.setPenColor(1201033);
        return underlineAnnotation;
    }

    @Override
    public AnnotationInfo annotatePdf() throws ParseException {
        setFixTop(false);
        AnnotationInfo underlineAnnotation = initAnnotationInfo();
        underlineAnnotation.setGuid(String.valueOf(annotationData.getId()));
        underlineAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        underlineAnnotation.setPenColor(1201033);
        return underlineAnnotation;
    }

    @Override
    public AnnotationInfo annotateCells() {
        throw new UnsupportedOperationException(String.format(MESSAGE, annotationData.getType()));
    }

    @Override
    public AnnotationInfo annotateSlides() throws ParseException {
        setFixTop(true);
        AnnotationInfo underlineAnnotation = initAnnotationInfo();
        underlineAnnotation.setPenColor(0);
        underlineAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        return underlineAnnotation;
    }

    @Override
    public AnnotationInfo annotateImage() {
        throw new UnsupportedOperationException(String.format(MESSAGE, annotationData.getType()));
    }

    @Override
    public AnnotationInfo annotateDiagram() {
        throw new UnsupportedOperationException(String.format(MESSAGE, annotationData.getType()));
    }

    @Override
    protected byte getType() {
        return AnnotationType.TextUnderline;
    }
}