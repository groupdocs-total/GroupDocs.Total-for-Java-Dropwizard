package com.groupdocs.ui.annotation.annotator;


import com.groupdocs.annotation.domain.*;
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
public class ResourceRedactionAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public ResourceRedactionAnnotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
        super(annotationData, documentInfo);
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateWord() throws ParseException {
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
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
            resourceRedactionAnnotation.setReplies(replies);
        }
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        return resourceRedactionAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
     */
    @Override
    public AnnotationInfo annotatePdf() throws ParseException {
        // initiate AnnotationInfo object
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set annotation X, Y position
        resourceRedactionAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
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
            resourceRedactionAnnotation.setReplies(replies);
        }
        // draw annotation options
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        // set page number to add annotation
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // set annotation type
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        return resourceRedactionAnnotation;
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
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set page number
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
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
            resourceRedactionAnnotation.setReplies(replies);
        }
        // set annotation type
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        // set draw annotation properties
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return resourceRedactionAnnotation;
    }

    /**
     * Add area annnotation into the image file
     */
    @Override
    public AnnotationInfo annotateImage() throws ParseException {
        // init annotation object
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set page number
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
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
            resourceRedactionAnnotation.setReplies(replies);
        }
        // set annotation type
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        // set draw annotation properties
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return resourceRedactionAnnotation;
    }

    /**
     * Add area annnotation into the AutoCad document
     */
    @Override
    public AnnotationInfo annotateDiagram() throws ParseException {
        // init annotation object
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set page number
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
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
            resourceRedactionAnnotation.setReplies(replies);
        }
        // set annotation type
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        // set draw annotation properties
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return resourceRedactionAnnotation;
    }
}