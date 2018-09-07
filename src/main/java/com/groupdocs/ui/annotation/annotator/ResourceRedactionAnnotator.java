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
    public ResourceRedactionAnnotator(AnnotationDataEntity annotationData){
        super(annotationData);
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateWord(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add annotation comment
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        if(comment != null) {
            resourceRedactionAnnotation.setText(comment.getText());
            resourceRedactionAnnotation.setCreatorName(comment.getUserName());
            Date date = format.parse(comment.getTime());
            resourceRedactionAnnotation.setCreatedOn(date);
        }
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        return resourceRedactionAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
     * @param info
     */
    @Override
    public AnnotationInfo annotatePdf(DocumentInfoContainer info) throws ParseException {
        // initiate AnnotationInfo object
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set annotation X, Y position
        resourceRedactionAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        // initiate reply info array
        AnnotationReplyInfo[] annotationReplyInfos = new AnnotationReplyInfo[annotationData.getComments().length];
        // add each reply
        for(int i = 0; i < annotationData.getComments().length; i++){
            // reply info object
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            // convert date time string into the date object
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            // set reply text
            reply.setMessage(annotationData.getComments()[i].getText());
            reply.setUserName(annotationData.getComments()[i].getUserName());
            annotationReplyInfos[i] = reply;
        }
        // add all replies
        resourceRedactionAnnotation.setReplies(annotationReplyInfos);
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
    public AnnotationInfo annotateCells(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * Add area annnotation into the Power Point document
     * @param info
     */
    @Override
    public AnnotationInfo annotateSlides(DocumentInfoContainer info) throws ParseException {
        // initiate AnnotationInfo object
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set page number
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add replies
        AnnotationReplyInfo[] annotationReplyInfos = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 0; i < annotationData.getComments().length; i++){
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setMessage(annotationData.getComments()[i].getText());
            reply.setUserName(annotationData.getComments()[i].getUserName());
            annotationReplyInfos[i] = reply;
        }
        resourceRedactionAnnotation.setReplies(annotationReplyInfos);
        // set annotation type
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        // set draw annotation properties
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return resourceRedactionAnnotation;
    }

    /**
     * Add area annnotation into the image file
     * @param info
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set page number
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add replies
        AnnotationReplyInfo[] annotationReplyInfos = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 0; i < annotationData.getComments().length; i++){
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setMessage(annotationData.getComments()[i].getText());
            reply.setUserName(annotationData.getComments()[i].getUserName());
            annotationReplyInfos[i] = reply;
        }
        resourceRedactionAnnotation.setReplies(annotationReplyInfos);
        // set annotation type
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        // set draw annotation properties
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return resourceRedactionAnnotation;
    }

    /**
     * Add area annnotation into the AutoCad document
     * @param info
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo resourceRedactionAnnotation = new AnnotationInfo();
        // set page number
        resourceRedactionAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add replies
        AnnotationReplyInfo[] annotationReplyInfos = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 0; i < annotationData.getComments().length; i++){
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setMessage(annotationData.getComments()[i].getText());
            reply.setUserName(annotationData.getComments()[i].getUserName());
            annotationReplyInfos[i] = reply;
        }
        resourceRedactionAnnotation.setReplies(annotationReplyInfos);
        // set annotation type
        resourceRedactionAnnotation.setType(AnnotationType.ResourcesRedaction);
        // set draw annotation properties
        resourceRedactionAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return resourceRedactionAnnotation;
    }
}