package com.groupdocs.ui.annotation.util;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.AnnotationReplyInfo;
import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.annotation.entity.web.CommentsEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Mapper for annotation entities
 */
public class AnnotationMapper {
    /**
     * Instance of mapper
     */
    public static final AnnotationMapper instance = new AnnotationMapper();

    private AnnotationMapper() {
    }

    /**
     * Map AnnotationInfo instances into AnnotationDataEntity
     *
     * @param annotations list of AnnotationInfo instances
     * @param pageNumber number of page for mapping
     * @return list of AnnotationDataEntity instances
     */
    public AnnotationDataEntity[] mapForPage(AnnotationInfo[] annotations, int pageNumber) {
        // initiate annotations data array
        AnnotationDataEntity[] pageAnnotations = new AnnotationDataEntity[annotations.length];
        // set each annotation data - this functionality used since annotations data returned by the
        // GroupDocs.Annotation library are obfuscated
        for (int n = 0; n < annotations.length; n++) {
            AnnotationInfo annotationInfo = annotations[n];
            if(pageNumber == annotationInfo.getPageNumber() + 1) {
                AnnotationDataEntity annotation = mapAnnotationDataEntity(annotationInfo);
                pageAnnotations[n] = annotation;
            }
        }
        return pageAnnotations;
    }

    /**
     * Map AnnotationInfo instances into AnnotationDataEntity
     *
     * @param annotationInfo AnnotationInfo instance
     * @return AnnotationDataEntity instance
     */
    public AnnotationDataEntity mapAnnotationDataEntity(AnnotationInfo annotationInfo) {
        AnnotationDataEntity annotation = new AnnotationDataEntity();
        annotation.setFont(annotationInfo.getFontFamily());
        annotation.setFontSize(annotationInfo.getFontSize());
        annotation.setHeight(annotationInfo.getBox().getHeight());
        annotation.setLeft(annotationInfo.getBox().getX());
        annotation.setPageNumber(annotationInfo.getPageNumber() + 1);
        annotation.setSvgPath(annotationInfo.getSvgPath());
        String text = (annotationInfo.getText() == null) ? annotationInfo.getFieldText() : annotationInfo.getText();
        annotation.setText(text);
        annotation.setTop(annotationInfo.getBox().getY());
        annotation.setType(AnnotationTypes.instance.getAnnotationType(annotationInfo.getType()));
        annotation.setWidth(annotationInfo.getBox().getWidth());
        // set each reply data
        AnnotationReplyInfo[] replies = annotationInfo.getReplies();
        if (replies != null && replies.length > 0) {
            CommentsEntity[] comments = new CommentsEntity[replies.length];
            for (int m = 0; m < replies.length; m++) {
                CommentsEntity comment = new CommentsEntity();
                AnnotationReplyInfo reply = replies[m];
                comment.setText(reply.getMessage());
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                comment.setTime(format.format(reply.getRepliedOn()));
                comment.setUserName(reply.getUserName());
                comments[m] = comment;
            }
            annotation.setComments(comments);
        }
        return annotation;
    }
}
