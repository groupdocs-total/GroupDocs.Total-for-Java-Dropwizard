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
    public ArrowAnnotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
        super(annotationData, documentInfo);
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateWord() throws ParseException {
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
        // calculate correct coordinates for the annotation - this is used since the GroupDocs.Annotation library counts the coordinates from the bottom of the document
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        arrowAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        arrowAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
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
            arrowAnnotation.setReplies(replies);
        }
        arrowAnnotation.setType(AnnotationType.Arrow);
        return arrowAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
     */
    @Override
    public AnnotationInfo annotatePdf() throws ParseException {
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
        // set draw annotation properties
        // calculate correct coordinates for the annotation - this is used since the GroupDocs.Annotation library counts the coordinates from the bottom of the document
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        arrowAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        arrowAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
        // sert annotation type
        arrowAnnotation.setType(AnnotationType.Arrow);
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
            arrowAnnotation.setReplies(replies);
        }
        return arrowAnnotation;
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
        // init annotation object
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
        // calculate correct coordinates for the annotation - this is used since the GroupDocs.Annotation library counts the coordinates from the bottom of the document
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        arrowAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        arrowAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
        // set type
        arrowAnnotation.setType(AnnotationType.Arrow);
        arrowAnnotation.setBackgroundColor(15988609);
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
        arrowAnnotation.setReplies(replies);
        return arrowAnnotation;
    }

    /**
     * Add area annnotation into the image file
     */
    @Override
    public AnnotationInfo annotateImage() throws ParseException {
        // init annotation object
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
        // calculate correct coordinates for the annotation - this is used since the GroupDocs.Annotation library counts the coordinates from the bottom of the document
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        arrowAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        arrowAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
        // set type
        arrowAnnotation.setType(AnnotationType.Arrow);
        arrowAnnotation.setBackgroundColor(-15988609);
        // add replies
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
            arrowAnnotation.setReplies(replies);
        }
        return arrowAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram() throws ParseException {
        // init annotation object
        AnnotationInfo arrowAnnotation = new AnnotationInfo();
        String startPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[0];
        String endPoint = annotationData.getSvgPath().replaceAll("[a-zA-Z]+", "").split(" ")[1];
        double startX =  Double.parseDouble(startPoint.split(",")[0]);
        double startY =  Double.parseDouble(startPoint.split(",")[1]);
        double endX =  Double.parseDouble(endPoint.split(",")[0]) - startX;
        double endY =  Double.parseDouble(endPoint.split(",")[1]) - startY;
        // set annotation position
        arrowAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        arrowAnnotation.setBox(new Rectangle(startX, startY, endX, endY));
        //set page number
        arrowAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        arrowAnnotation.setSvgPath("M" + startX + "," + startY  + "L" + endX + "," + endY);
        // set type
        arrowAnnotation.setType(AnnotationType.Arrow);
        arrowAnnotation.setBackgroundColor(15988609);
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
        arrowAnnotation.setReplies(replies);
        return arrowAnnotation;
    }
}