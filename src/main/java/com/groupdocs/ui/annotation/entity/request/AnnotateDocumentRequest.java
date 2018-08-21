package com.groupdocs.ui.annotation.entity.request;

import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;

import java.util.List;

public class AnnotateDocumentRequest extends LoadDocumentRequest {
    private  AnnotationDataEntity[] annotationssData;

    public AnnotationDataEntity[] getAnnotationssData() {
        return annotationssData;
    }

    public void setAnnotationssData(AnnotationDataEntity[] annotationssData) {
        this.annotationssData = annotationssData;
    }
}
