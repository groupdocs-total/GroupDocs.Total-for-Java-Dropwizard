package com.groupdocs.ui.annotation.annotator;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;

import javax.ws.rs.NotSupportedException;
import java.text.ParseException;

/**
 * Annotator
 * Abstract class contains general description for the annotating functionality
 * @author Aspose Pty Ltd
 */


public abstract class Annotator {
    protected AnnotationDataEntity annotationData;
    protected DocumentInfoContainer documentInfo;

    /**
     * Constructor
     * @param annotationData
     */
    public Annotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
        this.annotationData = annotationData;
        this.documentInfo = documentInfo;
    }

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateWord() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotatePdf() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateCells() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateSlides() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateImage() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract AnnotationInfo annotateDiagram() throws NotSupportedException, ParseException;
}
