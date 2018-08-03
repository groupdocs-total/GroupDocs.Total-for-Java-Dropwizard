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
 * TextAnnotator
 * Annotates documents with the text annotation
 * @author Aspose Pty Ltd
 */
public class PointAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public PointAnnotator(AnnotationDataEntity annotationData){
        super(annotationData);
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateWord(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        throw new NotSupportedException("This file type is not supported");
    }

    /**
     * Add area annnotation into the pdf document
     * @param info
     */
    @Override
    public AnnotationInfo annotatePdf(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo pointAnnotation = new AnnotationInfo();
        // set annotation position
        pointAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        // set draw annotation properties
        pointAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        //set page number
        pointAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // sert annotation type
        pointAnnotation.setType(AnnotationType.Point);
        // add replies
        AnnotationReplyInfo[] replyes = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 1; i < annotationData.getComments().length; i++) {
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            reply.setMessage(annotationData.getComments()[i].getText());
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setParentReplyGuid(String.valueOf(annotationData.getId()));
            replyes[i] = reply;
        }
        pointAnnotation.setReplies(replyes);
        return pointAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
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
        // init annotation object
        AnnotationInfo pointAnnotation = new AnnotationInfo();
        // set position
        pointAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        // set draw properties
        pointAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        //set page number
        pointAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // set type
        pointAnnotation.setType(AnnotationType.Point);
        // add replies
        AnnotationReplyInfo[] replyes = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 1; i < annotationData.getComments().length; i++) {
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            reply.setMessage(annotationData.getComments()[i].getText());
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setParentReplyGuid(String.valueOf(annotationData.getId()));
            replyes[i] = reply;
        }
        pointAnnotation.setReplies(replyes);
        return pointAnnotation;
    }

    /**
     * Add area annnotation into the image file
     * @param info
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo pointAnnotation = new AnnotationInfo();
        pointAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        pointAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        pointAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        pointAnnotation.setType(AnnotationType.Point);
        AnnotationReplyInfo[] replyes = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 1; i < annotationData.getComments().length; i++) {
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            reply.setMessage(annotationData.getComments()[i].getText());
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setParentReplyGuid(String.valueOf(annotationData.getId()));
            replyes[i] = reply;
        }
        pointAnnotation.setReplies(replyes);
        return pointAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        throw new NotSupportedException("This file type is not supported");
    }
}