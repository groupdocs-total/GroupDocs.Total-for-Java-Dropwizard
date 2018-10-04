package com.groupdocs.ui.annotation.entity.request;

import com.groupdocs.ui.annotation.entity.web.AnnotationDataEntity;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;

/**
 * AnnotateDocumentRequest
 * @author Aspose Pty Ltd
 */
public class AnnotateDocumentRequest extends LoadDocumentRequest {
    private String documentType;
    private AnnotationDataEntity[] annotationsData;

    public AnnotationDataEntity[] getAnnotationsData() {
        return annotationsData;
    }

    public void setAnnotationsData(AnnotationDataEntity[] annotationsData) {
        this.annotationsData = annotationsData;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
