package com.groupdocs.ui.annotation.entity.web;

import com.groupdocs.ui.common.entity.web.PageDescriptionEntity;

public class PageDataDescriptionEntity extends PageDescriptionEntity {
    /**
     * Annotation data
     */
    private String data;
    /**
     * List of annotation data
     */
    private AnnotationDataEntity[] annotations;

    public AnnotationDataEntity[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(AnnotationDataEntity[] annotations) {
        this.annotations = annotations;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
