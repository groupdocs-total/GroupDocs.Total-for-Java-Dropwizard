package com.groupdocs.ui.editor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupdocs.ui.common.config.CommonConfiguration;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;

import static com.groupdocs.ui.common.config.DefaultDirectories.defaultEditorDirectory;
import static com.groupdocs.ui.common.config.DefaultDirectories.relativePathToAbsolute;

public class EditorConfigurationModel {

    @Valid
    @JsonProperty
    private String filesDirectory;

    @Valid
    @JsonProperty
    private String fontsDirectory;

    @Valid
    @JsonProperty
    private String defaultDocument;

    @Valid
    @JsonProperty
    private Boolean createNewFile;

    @Valid
    @JsonProperty
    private boolean pageSelector;

    @Valid
    @JsonProperty
    private boolean download;

    @Valid
    @JsonProperty
    private boolean upload;

    @Valid
    @JsonProperty
    private boolean print;

    @Valid
    @JsonProperty
    private boolean browse;

    @Valid
    @JsonProperty
    private boolean rewrite;

    @Valid
    @JsonProperty
    private boolean enableRightClick;

    public static EditorConfigurationModel createEditorConfiguration(EditorConfiguration editor, CommonConfiguration common) {
        EditorConfigurationModel config = new EditorConfigurationModel();
        config.setCreateNewFile(editor.getCreateNewFile());
        config.setFilesDirectory(editor.getFilesDirectory());
        config.setDefaultDocument(editor.getDefaultDocument());
        config.setFontsDirectory(editor.getFontsDirectory());
        config.setPageSelector(common.isPageSelector());
        config.setDownload(common.isDownload());
        config.setUpload(common.isUpload());
        config.setPrint(common.isPrint());
        config.setBrowse(common.isBrowse());
        config.setRewrite(common.isRewrite());
        config.setEnableRightClick(common.isEnableRightClick());
        return config;
    }

    public String getFilesDirectory() {
        return filesDirectory;
    }

    public void setFilesDirectory(String filesDirectory) {
        this.filesDirectory = StringUtils.isEmpty(filesDirectory) ? defaultEditorDirectory() : relativePathToAbsolute(filesDirectory);
    }

    public String getFontsDirectory() {
        return fontsDirectory;
    }

    public void setFontsDirectory(String fontsDirectory) {
        this.fontsDirectory = fontsDirectory;
    }

    public String getDefaultDocument() {
        return defaultDocument;
    }

    public void setDefaultDocument(String defaultDocument) {
        this.defaultDocument = defaultDocument;
    }

    public Boolean getCreateNewFile() {
        return createNewFile;
    }

    public void setCreateNewFile(Boolean createNewFile) {
        this.createNewFile = createNewFile;
    }

    public boolean isPageSelector() {
        return pageSelector;
    }

    public void setPageSelector(boolean pageSelector) {
        this.pageSelector = pageSelector;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public boolean isBrowse() {
        return browse;
    }

    public void setBrowse(boolean browse) {
        this.browse = browse;
    }

    public boolean isRewrite() {
        return rewrite;
    }

    public void setRewrite(boolean rewrite) {
        this.rewrite = rewrite;
    }

    public boolean isEnableRightClick() {
        return enableRightClick;
    }

    public void setEnableRightClick(boolean enableRightClick) {
        this.enableRightClick = enableRightClick;
    }

    @Override
    public String toString() {
        return "EditorConfigurationModel{" +
                "filesDirectory='" + filesDirectory + '\'' +
                ", fontsDirectory='" + fontsDirectory + '\'' +
                ", defaultDocument='" + defaultDocument + '\'' +
                ", createNewFile=" + createNewFile +
                ", pageSelector=" + pageSelector +
                ", download=" + download +
                ", upload=" + upload +
                ", print=" + print +
                ", browse=" + browse +
                ", rewrite=" + rewrite +
                ", enableRightClick=" + enableRightClick +
                '}';
    }
}
