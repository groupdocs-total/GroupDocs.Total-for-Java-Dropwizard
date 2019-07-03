package com.groupdocs.ui.editor.service;

import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.LoadDocumentEntity;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;
import com.groupdocs.ui.editor.model.EditDocumentRequest;
import com.groupdocs.ui.editor.model.EditorConfiguration;

import java.util.List;
import java.util.Set;

public interface EditorService {
    List<FileDescriptionEntity> getFileList(String path);

    EditorConfiguration getEditorConfiguration();

    LoadDocumentEntity loadDocument(LoadDocumentRequest loadDocumentRequest);

    /**
     * Get supported formats
     *
     * @return
     */
    Set<String> getSupportedFormats();

    LoadDocumentEntity saveDoc(EditDocumentRequest loadDocumentEntity);
}
