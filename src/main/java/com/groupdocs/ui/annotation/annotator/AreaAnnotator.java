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
    public AreaAnnotator(AnnotationDataEntity annotationData){
        super(annotationData);
    }

    /**
     * This file type doesn't supported for the current annotation type
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
        // initiate AnnotationInfo object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set annotation X, Y position
        areaAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
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
        areaAnnotation.setReplies(annotationReplyInfos);
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
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set page number
        areaAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
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
        areaAnnotation.setReplies(annotationReplyInfos);
        // set annotation type
        areaAnnotation.setType(AnnotationType.Area);
        // set draw annotation properties
        areaAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return areaAnnotation;
    }

    /**
     * Add area annnotation into the image file
     * @param info
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set page number
        areaAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
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
        areaAnnotation.setReplies(annotationReplyInfos);
        // set annotation type
        areaAnnotation.setType(AnnotationType.Area);
        // set draw annotation properties
        areaAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return areaAnnotation;
    }

    /**
     * Add area annnotation into the AutoCad document
     * @param info
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        // set page number
        areaAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
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
        areaAnnotation.setReplies(annotationReplyInfos);
        // set annotation type
        areaAnnotation.setType(AnnotationType.Area);
        // set draw annotation properties
        areaAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        return areaAnnotation;
    }
}