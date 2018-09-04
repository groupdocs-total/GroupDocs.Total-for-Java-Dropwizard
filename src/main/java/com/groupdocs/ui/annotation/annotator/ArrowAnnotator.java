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
public class ArrowAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public ArrowAnnotator(AnnotationDataEntity annotationData){
        super(annotationData);
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateWord(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
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
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // add annotation comment
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        if(comment != null) {
            arrowAnnotation.setText(comment.getText());
            arrowAnnotation.setCreatorName(comment.getUserName());
            Date date = format.parse(comment.getTime());
            arrowAnnotation.setCreatedOn(date);
        }
        arrowAnnotation.setType(AnnotationType.Arrow);
        return arrowAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
     * @param info
     */
    @Override
    public AnnotationInfo annotatePdf(DocumentInfoContainer info) throws ParseException {
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
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
        arrowAnnotation.setAnnotationPosition(new Point(startX, startY));
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        arrowAnnotation.setSvgPath(annotationData.getSvgPath());
        // sert annotation type
        arrowAnnotation.setType(AnnotationType.Arrow);
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
        arrowAnnotation.setReplies(replyes);
        return arrowAnnotation;
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
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
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
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // set type
        arrowAnnotation.setType(AnnotationType.Arrow);
        arrowAnnotation.setBackgroundColor(15988609);
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
        arrowAnnotation.setReplies(replyes);
        return arrowAnnotation;
    }

    /**
     * Add area annnotation into the image file
     * @param info
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
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
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));        
        // set type
        arrowAnnotation.setType(AnnotationType.Arrow);
        arrowAnnotation.setBackgroundColor(15988609);
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
        arrowAnnotation.setReplies(replyes);
        return arrowAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        // init annotation object
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
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
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // set type
        arrowAnnotation.setType(AnnotationType.Arrow);
        arrowAnnotation.setBackgroundColor(15988609);
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
        arrowAnnotation.setReplies(replyes);
        return arrowAnnotation;
    }
}