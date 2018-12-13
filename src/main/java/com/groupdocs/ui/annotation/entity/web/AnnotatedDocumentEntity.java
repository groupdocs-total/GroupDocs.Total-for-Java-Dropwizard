package com.groupdocs.ui.annotation.entity.web;

import com.groupdocs.ui.common.entity.web.DocumentDescriptionEntity;

/**
 * AnnotatedDocumentEntity
 *
 * @author Aspose Pty Ltd
 */
public class AnnotatedDocumentEntity extends DocumentDescriptionEntity {
    /**
     * Document Guid
     */
    private String guid;
    /**
     * List of annotation data
     */
    private AnnotationDataEntity[] annotations;
    /**
     * Annotation data
     */
    private String data;
    /**
     * List of supported types of annotations
     */
    public String[] supportedAnnotations;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public AnnotationDataEntity[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(AnnotationDataEntity[] annotations) {
        this.annotations = annotations;
    }

    public String getData(){return data;}

    public void setData(String image){ this.data = image;}

    public String[] getSupportedAnnotations() {
        return supportedAnnotations;
    }

    public void setSupportedAnnotations(String[] supportedAnnotations) {
        this.supportedAnnotations = supportedAnnotations;
    }
}
