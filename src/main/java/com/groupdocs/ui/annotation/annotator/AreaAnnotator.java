package com.groupdocs.ui.annotation.annotator;


import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.Point;
import com.groupdocs.annotation.domain.AnnotationReplyInfo;
import com.groupdocs.annotation.domain.Rectangle;
import com.groupdocs.annotation.domain.AnnotationType;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.annotation.entity.web.CommentsEntity;

import javax.ws.rs.NotSupportedException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * AreaAnnotator
 * Annotates documents with the area annotation
 * @author Aspose Pty Ltd
 */
public class AreaAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public AreaAnnotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
        super(annotationData, documentInfo);
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateWord() throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * Add area annotation into the pdf document
     */
    @Override
    public AnnotationInfo annotatePdf() throws ParseException {
        // initiate AnnotationInfo object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set annotation X, Y position
        areaAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        // add replies
        if(annotationData.getComments() != null && annotationData.getComments().length != 0) {
            AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
            for (int i = 0; i < annotationData.getComments().length; i++) {
                AnnotationReplyInfo reply = new AnnotationReplyInfo();
                reply.setMessage(annotationData.getComments()[i].getText());
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = format.parse(annotationData.getComments()[i].getTime());
                reply.setRepliedOn(date);
                reply.setUserName(annotationData.getComments()[i].getUserName());
                replies[i] = reply;
            }
            areaAnnotation.setReplies(replies);
        }
        // draw annotation options
        areaAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        // set page number to add annotation
        areaAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // set annotation type
        areaAnnotation.setType(AnnotationType.Area);
        return areaAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
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
        // initiate AnnotationInfo object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set page number
        areaAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add replies
        if(annotationData.getComments() != null && annotationData.getComments().length != 0) {
            AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
            for (int i = 0; i < annotationData.getComments().length; i++) {
                AnnotationReplyInfo reply = new AnnotationReplyInfo();
                reply.setMessage(annotationData.getComments()[i].getText());
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = format.parse(annotationData.getComments()[i].getTime());
                reply.setRepliedOn(date);
                reply.setUserName(annotationData.getComments()[i].getUserName());
                replies[i] = reply;
            }
            areaAnnotation.setReplies(replies);
        }
        // set annotation type
        areaAnnotation.setType(AnnotationType.Area);
        // set draw annotation properties
        areaAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return areaAnnotation;
    }

    /**
     * Add area annnotation into the image file
    
     */
    @Override
    public AnnotationInfo annotateImage() throws ParseException {
        // init annotation object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set page number
        areaAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add replies
        if(annotationData.getComments() != null && annotationData.getComments().length != 0) {
            AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
            for (int i = 0; i < annotationData.getComments().length; i++) {
                AnnotationReplyInfo reply = new AnnotationReplyInfo();
                reply.setMessage(annotationData.getComments()[i].getText());
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = format.parse(annotationData.getComments()[i].getTime());
                reply.setRepliedOn(date);
                reply.setUserName(annotationData.getComments()[i].getUserName());
                replies[i] = reply;
            }
            areaAnnotation.setReplies(replies);
        }
        // set annotation type
        areaAnnotation.setType(AnnotationType.Area);
        // set draw annotation properties
        areaAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return areaAnnotation;
    }

    /**
     * Add area annnotation into the AutoCad document
    
     */
    @Override
    public AnnotationInfo annotateDiagram() throws ParseException {
        // init annotation object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set page number
        areaAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add replies
        if(annotationData.getComments() != null && annotationData.getComments().length != 0) {
            AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
            for (int i = 0; i < annotationData.getComments().length; i++) {
                AnnotationReplyInfo reply = new AnnotationReplyInfo();
                reply.setMessage(annotationData.getComments()[i].getText());
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = format.parse(annotationData.getComments()[i].getTime());
                reply.setRepliedOn(date);
                reply.setUserName(annotationData.getComments()[i].getUserName());
                replies[i] = reply;
            }
            areaAnnotation.setReplies(replies);
        }
        // set annotation type
        areaAnnotation.setType(AnnotationType.Area);
        // set draw annotation properties
        areaAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return areaAnnotation;
    }
}