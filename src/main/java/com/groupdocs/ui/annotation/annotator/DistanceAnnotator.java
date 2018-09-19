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
    public DistanceAnnotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
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
        AnnotationInfo distanceAnnotation = new AnnotationInfo();
        // set draw annotation properties
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        distanceAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        distanceAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        distanceAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        distanceAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
        // sert annotation type
        distanceAnnotation.setType(AnnotationType.Distance);
        // add replies
        String text = (annotationData.getText() == null) ? "" : annotationData.getText();
        if(annotationData.getComments() != null && annotationData.getComments().length != 0) {
            AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
            for (int i = 0; i < annotationData.getComments().length; i++) {
                AnnotationReplyInfo reply = new AnnotationReplyInfo();
                if(i == 0){
                    reply.setMessage(text + " " + annotationData.getComments()[i].getText());
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
        } else {
            distanceAnnotation.setFieldText(text);
        }
        return distanceAnnotation;
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
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * Add area annnotation into the image file
     */
    @Override
    public AnnotationInfo annotateImage() throws ParseException {
        // init annotation object
        AnnotationInfo distanceAnnotation = new AnnotationInfo();
        // set draw annotation properties
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        distanceAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        distanceAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        distanceAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
        // set type
        distanceAnnotation.setType(AnnotationType.Distance);
        distanceAnnotation.setBackgroundColor(15988609);
        // add replies
        if(annotationData.getComments() != null && annotationData.getComments().length != 0) {
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
        } else {
            distanceAnnotation.setFieldText(annotationData.getText());
        }
        return distanceAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram() throws ParseException {
        // init annotation object
        AnnotationInfo distanceAnnotation = new AnnotationInfo();
        // set draw annotation properties
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        distanceAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        distanceAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        distanceAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
        //set page number
        distanceAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // set type
        distanceAnnotation.setType(AnnotationType.Distance);
        distanceAnnotation.setBackgroundColor(15988609);
        // add replies
        if(annotationData.getComments() != null && annotationData.getComments().length != 0) {
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
        } else {
            distanceAnnotation.setFieldText(annotationData.getText());
        }
        return distanceAnnotation;
    }
}