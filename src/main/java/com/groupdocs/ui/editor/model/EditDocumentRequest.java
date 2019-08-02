package com.groupdocs.ui.editor.model;

import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;

public class EditDocumentRequest extends LoadDocumentRequest {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
