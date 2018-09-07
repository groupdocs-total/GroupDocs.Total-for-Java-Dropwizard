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
public class DistanceAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public DistanceAnnotator(AnnotationDataEntity annotationData){
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
        AnnotationInfo distanceAnnotation = new AnnotationInfo();
        // set draw annotation properties
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]);
        double endY =  Double.parseDouble(endPoint.split(",")[1]);
        double positionShift = 0;
        if(startX > endX){
            positionShift = startX - endX;
            endX = -positionShift;
        } else {
            endX = endX - startX;
        }
        if(startY > endY){
            positionShift = startY - endY;
            endY = -positionShift;
        } else {
            endY = endY - startY;
        }
        // set annotation position
        distanceAnnotation.setAnnotationPosition(new Point(startX, startY));
        distanceAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        distanceAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        distanceAnnotation.setSvgPath(annotationData.getSvgPath());
        // sert annotation type
        distanceAnnotation.setType(AnnotationType.Distance);
        // add replies
        if(annotationData.getComments().length != 0) {
            AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
            for (int i = 0; i < annotationData.getComments().length; i++) {
                AnnotationReplyInfo reply = new AnnotationReplyInfo();
                if(i == 0){
                    reply.setMessage(annotationData.getText() + " " + annotationData.getComments()[i].getText());
                } else {
                    reply.setMessage(annotationData.getComments()[i].getText());
                }
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = format.parse(annotationData.getComments()[i].getTime());
                reply.setRepliedOn(date);
                reply.setUserName(annotationData.getComments()[i].getUserName());
                replies[i] = reply;
            }
            distanceAnnotation.setReplies(replies);
        }
        return distanceAnnotation;
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
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * Add area annnotation into the image file
     * @param info
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo distanceAnnotation = new AnnotationInfo();
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]);
        double endY =  Double.parseDouble(endPoint.split(",")[1]);
        double positionShift = 0;
        if(startX > endX){
            positionShift = startX - endX;
            endX = -positionShift;
        } else {
            endX = endX - startX;
        }
        if(startY > endY){
            positionShift = startY - endY;
            endY = -positionShift;
        } else {
            endY = endY - startY;
        }
        distanceAnnotation.setBox(new Rectangle(startX, startY, endX, endY));        
        // set type
        distanceAnnotation.setType(AnnotationType.Distance);
        distanceAnnotation.setBackgroundColor(15988609);
        // add replies
        if(annotationData.getComments().length != 0) {
            AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
            for (int i = 0; i < annotationData.getComments().length; i++) {
                AnnotationReplyInfo reply = new AnnotationReplyInfo();
                if(i == 0){
                    reply.setMessage(annotationData.getText() + " " + annotationData.getComments()[i].getText());
                } else {
                    reply.setMessage(annotationData.getComments()[i].getText());
                }
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = format.parse(annotationData.getComments()[i].getTime());
                reply.setRepliedOn(date);
                reply.setUserName(annotationData.getComments()[i].getUserName());
                replies[i] = reply;
            }
            distanceAnnotation.setReplies(replies);
        }
        return distanceAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo distanceAnnotation = new AnnotationInfo();
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]);
        double endY =  Double.parseDouble(endPoint.split(",")[1]);
        double positionShift = 0;
        if(startX > endX){
            positionShift = startX - endX;
            endX = -positionShift;
        } else {
            endX = endX - startX;
        }
        if(startY > endY){
            positionShift = startY - endY;
            endY = -positionShift;
        } else {
            endY = endY - startY;
        }
        distanceAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        distanceAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // set type
        distanceAnnotation.setType(AnnotationType.Distance);
        distanceAnnotation.setBackgroundColor(15988609);
        // add replies
        AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 1; i < annotationData.getComments().length; i++) {
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            reply.setMessage(annotationData.getComments()[i].getText());
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setParentReplyGuid(String.valueOf(annotationData.getId()));
            reply.setUserName(annotationData.getComments()[i].getUserName());
            replies[i] = reply;
        }
        distanceAnnotation.setReplies(replies);
        return distanceAnnotation;
    }
}