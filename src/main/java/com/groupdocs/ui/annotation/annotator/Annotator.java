package com.groupdocs.ui.annotation.annotator;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.annotation.entity.web.CommentsEntity;

import javax.ws.rs.NotSupportedException;
import java.text.ParseException;

/**
 * Annotator
 * Abstract class contains general description for the annotating functionality
 * @author Aspose Pty Ltd
 */


public abstract class Annotator {
    protected AnnotationDataEntity annotationData;

    /**
     * Constructor
     * @param annotationData
     */
    public Annotator(AnnotationDataEntity annotationData){
        this.annotationData = annotationData;
    }

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateWord(DocumentInfoContainer info, CommentsEntity comment) throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotatePdf(DocumentInfoContainer info) throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateCells(DocumentInfoContainer info, CommentsEntity comment) throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateSlides(DocumentInfoContainer info) throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateImage(DocumentInfoContainer info) throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws NotSupportedException, ParseException;
}
