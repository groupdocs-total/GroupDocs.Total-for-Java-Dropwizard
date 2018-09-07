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
public class TexStrikeoutAnnotator extends Annotator{

    /**
     * Constructor
     * @param annotationData
     */
    public TexStrikeoutAnnotator(AnnotationDataEntity annotationData){
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
        AnnotationInfo strikeoutAnnotation = new AnnotationInfo();
        strikeoutAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        // we use such calculation since the GroupDocs.Annotation library takes text line position from the bottom of the page
        double topPosition = info.getPages().get(annotationData.getPageNumber() - 1).getHeight() - annotationData.getTop();
        // calculation of the X-shift
        double topRightX = annotationData.getLeft() + annotationData.getWidth();
        // calculation of the Y-shift
        double bottomRightY = topPosition - annotationData.getHeight();
        // set  draw annotation properties
        strikeoutAnnotation.setBox(new Rectangle(annotationData.getLeft(), topPosition, annotationData.getWidth(), annotationData.getHeight()));
        strikeoutAnnotation.setSvgPath(
                "[{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + bottomRightY +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + bottomRightY + "}]");
        // set annotation type
        strikeoutAnnotation.setType(AnnotationType.TextStrikeout);
        // add annotation comment
        strikeoutAnnotation.setText(comment.getText());
        strikeoutAnnotation.setCreatorName(comment.getUserName());
        // set line color
        strikeoutAnnotation.setPenColor(1201033);
        // set date
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = format.parse(comment.getTime());
        strikeoutAnnotation.setCreatedOn(date);
        return strikeoutAnnotation;
    }

    /**
     * Add area annnotation into the pdf document
     * @param info
     */
    @Override
    public AnnotationInfo annotatePdf(DocumentInfoContainer info) throws ParseException {
        AnnotationInfo strikeoutAnnotation = new AnnotationInfo();
        strikeoutAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        strikeoutAnnotation.setBox(new Rectangle(annotationData.getLeft(), annotationData.getTop(), annotationData.getWidth(), annotationData.getHeight()));
        strikeoutAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        strikeoutAnnotation.setPenColor(0);
        strikeoutAnnotation.setType(AnnotationType.TextStrikeout);
        strikeoutAnnotation.setText(annotationData.getComments()[0].getText());
        strikeoutAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
        AnnotationReplyInfo[] replies = new AnnotationReplyInfo[annotationData.getComments().length];
        for(int i = 1; i < annotationData.getComments().length; i++) {
            AnnotationReplyInfo reply = new AnnotationReplyInfo();
            reply.setMessage(annotationData.getComments()[i].getText());
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(annotationData.getComments()[i].getTime());
            reply.setRepliedOn(date);
            reply.setUserName(annotationData.getComments()[i].getUserName());
            reply.setParentReplyGuid(String.valueOf(annotationData.getId()));
            replies[i] = reply;
        }
        strikeoutAnnotation.setReplies(replies);
        return strikeoutAnnotation;
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
        AnnotationInfo strikeoutAnnotation = new AnnotationInfo();
        strikeoutAnnotation.setAnnotationPosition(new Point(annotationData.getLeft(), annotationData.getTop()));
        double topPosition = info.getPages().get(annotationData.getPageNumber() - 1).getHeight() - annotationData.getTop();
        double topRightX = annotationData.getLeft() + annotationData.getWidth();
        double bottomRightY = topPosition - annotationData.getHeight();
        strikeoutAnnotation.setBox(new Rectangle(annotationData.getLeft(), topPosition, annotationData.getWidth(), annotationData.getHeight()));
        strikeoutAnnotation.setSvgPath(
                "[{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + topPosition +
                        "},{\"x\":" + annotationData.getLeft() +
                        ",\"y\":" + bottomRightY +
                        "},{\"x\":" + topRightX +
                        ",\"y\":" + bottomRightY + "}]");
        strikeoutAnnotation.setPageNumber(annotationData.getPageNumber() - 1);
        strikeoutAnnotation.setPenColor(0);
        strikeoutAnnotation.setType(AnnotationType.TextStrikeout);
        strikeoutAnnotation.setText(annotationData.getComments()[0].getText());
        strikeoutAnnotation.setCreatorName(annotationData.getComments()[0].getUserName());
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
        strikeoutAnnotation.setReplies(replies);
        return strikeoutAnnotation;
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