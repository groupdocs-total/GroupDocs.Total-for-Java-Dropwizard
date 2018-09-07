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
public class TexUnderlineAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public TexUnderlineAnnotator(AnnotationDataEntity annotationData){
        super(annotationData);
    }

    /**
     * Add area annnotation into the Word document
     * @param info
     * @param comment
     */
    @Override
    public AnnotationInfo annotateWord(DocumentInfoContainer info, CommentsEntity comment) throws ParseException {
        // init possible types of annotations
        AnnotationInfo underlineAnnotation = new AnnotationInfo();
        underlineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // we use such calculation since the GroupDocs.Annotation library takes text line position from the bottom of the page
        double topPosition = info.getPages().get(annotationData.getPageNumber() - 1).getHeight() - annotationData.getTop();
        // calculation of the X-shift
        double topRightX = annotationData.getLeft() + annotationData.getWidth();
        // calculation of the Y-shift
        double bottomRightY = topPosition - annotationData.getHeight();
        // set  draw annotation properties
        underlineAnnotation.setBox(new Rectangle(annotationData.getLeft(), topPosition, annotationData.getWidth(), annotationData.getHeight()));
        underlineAnnotation.setSvgPath(
                "[{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + bottomRightY +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + bottomRightY + "}]");
        // set annotation type
        underlineAnnotation.setType(AnnotationType.TextUnderline);
        // add annotation comment
        if(comment != null) {
            underlineAnnotation.setText(comment.getText());
            underlineAnnotation.setCreatorName(comment.getUserName());
            // set date
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(comment.getTime());
            underlineAnnotation.setCreatedOn(date);
        }
        // set line color
        underlineAnnotation.setPenColor(1201033);
        return underlineAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
     * @param info
     */
    @Override
    public AnnotationInfo annotatePdf(DocumentInfoContainer info) throws ParseException {
        AnnotationInfo underlineAnnotation = new AnnotationInfo();
        underlineAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        underlineAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        underlineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        underlineAnnotation.setPenColor(1201033);
        underlineAnnotation.setType(AnnotationType.TextUnderline);
        if(annotationData.getComments().length != 0) {
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
            underlineAnnotation.setReplies(replies);

        }
        return underlineAnnotation;
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
        AnnotationInfo underlineAnnotation = new AnnotationInfo();
        underlineAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        double topPosition = info.getPages().get(annotationData.getPageNumber() - 1).getHeight() - annotationData.getTop();
        double topRightX = annotationData.getLeft() + annotationData.getWidth();
        double bottomRightY = topPosition - annotationData.getHeight();
        underlineAnnotation.setBox(new Rectangle(annotationData.getLeft(), topPosition, annotationData.getWidth(), annotationData.getHeight()));
        underlineAnnotation.setSvgPath(
                "[{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + bottomRightY +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + bottomRightY + "}]");
        underlineAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        underlineAnnotation.setPenColor(0);
        underlineAnnotation.setType(AnnotationType.TextUnderline);
        underlineAnnotation.setText(annotationData.getComments()[0].getText());
        underlineAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
        underlineAnnotation.setReplies(replies);
        return underlineAnnotation;
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateImage(DocumentInfoContainer info) throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }

    /**
     * This file type doesn't supported for the current annotation type
     */
    @Override
    public AnnotationInfo annotateDiagram(DocumentInfoContainer info) throws ParseException {
        throw new NotSupportedException("Annotation of type " + annotationData.getType() + " for this file type is not supported");
    }
}