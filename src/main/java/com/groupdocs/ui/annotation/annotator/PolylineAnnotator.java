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
 * TextAnnotator
 * Annotates documents with the text annotation
 * @author Aspose Pty Ltd
 */
public class PolylineAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public PolylineAnnotator(AnnotationDataEntity annotationData){
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
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        polylineAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        polylineAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        polylineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setType(AnnotationType.Polyline);
        polylineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
            reply.setUserName(annotationData.getComments()[i].getUserName());
            replyes[i] = reply;
        }
        polylineAnnotation.setReplies(replyes);
        return polylineAnnotation;
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
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        polylineAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        polylineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setType(AnnotationType.Polyline);
        polylineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
            reply.setUserName(annotationData.getComments()[i].getUserName());
            replyes[i] = reply;
        }
        polylineAnnotation.setReplies(replyes);
        return polylineAnnotation;
    }

    /**
     * Add area annnotation into the image file
     * @param info
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        // polylineAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        polylineAnnotation.setType(AnnotationType.Polyline);
        polylineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
            reply.setUserName(annotationData.getComments()[i].getUserName());
            replyes[i] = reply;
        }
        polylineAnnotation.setReplies(replyes);
        return polylineAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        polylineAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        polylineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setType(AnnotationType.Polyline);
        polylineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
            reply.setUserName(annotationData.getComments()[i].getUserName());
            replyes[i] = reply;
        }
        polylineAnnotation.setReplies(replyes);
        return polylineAnnotation;
    }
}