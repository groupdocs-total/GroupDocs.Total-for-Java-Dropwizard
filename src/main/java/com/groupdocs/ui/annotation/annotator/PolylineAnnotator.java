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
    public PolylineAnnotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
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
     * Add area annnotation into the pdf document
     */
    @Override
    public AnnotationInfo annotatePdf() throws ParseException {
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        polylineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setType(AnnotationType.Polyline);
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
            polylineAnnotation.setReplies(replies);
        }
        return polylineAnnotation;
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
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        polylineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setType(AnnotationType.Polyline);
        polylineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
            polylineAnnotation.setReplies(replies);
        }
        return polylineAnnotation;
    }

    /**
     * Add area annnotation into the image file
     */
    @Override
    public AnnotationInfo annotateImage() throws ParseException {
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setType(AnnotationType.Polyline);
        polylineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
            polylineAnnotation.setReplies(replies);
        }
        return polylineAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram() throws ParseException {
        AnnotationInfo polylineAnnotation = new AnnotationInfo();
        polylineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        polylineAnnotation.setPenColor(1201033);
        polylineAnnotation.setPenWidth((byte) 2);
        polylineAnnotation.setSvgPath(annotationData.getSvgPath());
        polylineAnnotation.setType(AnnotationType.Polyline);
        polylineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
            polylineAnnotation.setReplies(replies);
        }
        return polylineAnnotation;
    }
}