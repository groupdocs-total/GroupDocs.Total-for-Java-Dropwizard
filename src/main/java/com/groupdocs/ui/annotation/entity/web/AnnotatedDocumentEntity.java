package com.groupdocs.ui.annotation.entity.web;

import com.groupdocs.ui.common.entity.web.DocumentDescriptionEntity;

/**
 * AnnotatedDocumentEntity
 * @author Aspose Pty Ltd
 */
public class AnnotatedDocumentEntity extends DocumentDescriptionEntity {
    private String guid;
    private AnnotationDataEntity[] annotations;

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
}
