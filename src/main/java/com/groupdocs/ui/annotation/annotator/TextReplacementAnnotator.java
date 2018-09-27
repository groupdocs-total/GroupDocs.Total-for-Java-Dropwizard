package com.groupdocs.ui.annotation.annotator;

import com.groupdocs.annotation.domain.*;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;

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
public class TextReplacementAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public TextReplacementAnnotator(AnnotationDataEntity annotationData, DocumentInfoContainer documentInfo){
        super(annotationData, documentInfo);
    }

    /**
     * Add area annnotation into the Word document
     */
    @Override
    public AnnotationInfo annotateWord() throws ParseException {
        // init possible types of annotations
        AnnotationInfo textReplacementAnnotation = new AnnotationInfo();
        textReplacementAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // we use such calculation since the GroupDocs.Annotation library takes text line position from the bottom of the page
        double topPosition = documentInfo.getPages().get(annotationData.getPageNumber() - 1).getHeight() - annotationData.getTop();
        double leftPosition = documentInfo.getPages().get(annotationData.getPageNumber() - 1).getWidth() - annotationData.getLeft();
        double topRightX = annotationData.getLeft() + annotationData.getWidth();
        double bottomRightY = topPosition - annotationData.getHeight();
        textReplacementAnnotation.setSvgPath(
                "[{\"x\":" + leftPosition +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + leftPosition +
                        ",\"y\":" + bottomRightY +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + bottomRightY + "}]");
        textReplacementAnnotation.setType(AnnotationType.TextReplacement);
        textReplacementAnnotation.setGuid(String.valueOf(annotationData.getId()));
        textReplacementAnnotation.setFieldText(annotationData.getText());
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
            textReplacementAnnotation.setReplies(replies);
        }
        return textReplacementAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
     */
    @Override
    public AnnotationInfo annotatePdf() throws ParseException {
        // init possible types of annotations
        AnnotationInfo textReplacementAnnotation = new AnnotationInfo();
        textReplacementAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // we use such calculation since the GroupDocs.Annotation library takes text line position from the bottom of the page
        double topPosition = documentInfo.getPages().get(annotationData.getPageNumber() - 1).getHeight() - annotationData.getTop();
        textReplacementAnnotation.setBox(new Rectangle(annotationData.getLeft(), topPosition,  annotationData.getWidth(),  annotationData.getHeight()));
        textReplacementAnnotation.setType(AnnotationType.TextReplacement);
        textReplacementAnnotation.setGuid( String.valueOf(annotationData.getId()));
        textReplacementAnnotation.setFieldText(annotationData.getText());
        double topRightX = annotationData.getLeft() + annotationData.getWidth();
        double bottomRightY = topPosition - annotationData.getHeight();
        textReplacementAnnotation.setSvgPath(
                "[{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + bottomRightY +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + bottomRightY + "}]");
        textReplacementAnnotation.setGuid( String.valueOf(annotationData.getId()));
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
            textReplacementAnnotation.setReplies(replies);
        }
        return textReplacementAnnotation;
    }

    /**
     * Add area annnotation into the Excel document
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
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateImage() throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram() throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }
}