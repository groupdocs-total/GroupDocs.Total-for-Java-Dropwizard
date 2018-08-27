package com.groupdocs.ui.annotation.entity.request;

import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;

public class AnnotateDocumentRequest extends LoadDocumentRequest {
    private AnnotationDataEntity[] annotationsList;

    public AnnotationDataEntity[] getAnnotationsData() {
        return annotationsList;
    }

    public void setAnnotationsData(AnnotationDataEntity[] annotationsData) {
        this.annotationsList = annotationsData;
    }
}
