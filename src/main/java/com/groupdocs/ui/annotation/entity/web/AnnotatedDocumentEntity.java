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

    private String data;

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
}
