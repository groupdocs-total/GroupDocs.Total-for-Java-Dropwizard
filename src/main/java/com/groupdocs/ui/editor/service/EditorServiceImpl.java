package com.groupdocs.ui.editor.service;

import com.google.common.collect.Sets;
import com.groupdocs.editor.EditorHandler;
import com.groupdocs.editor.InputHtmlDocument;
import com.groupdocs.editor.OutputHtmlDocument;
import com.groupdocs.editor.cells.htmltocells.CellFormats;
import com.groupdocs.editor.cells.htmltocells.CellsSaveOptions;
import com.groupdocs.editor.license.License;
import com.groupdocs.editor.options.IDocumentSaveOptions;
import com.groupdocs.editor.options.PdfSaveOptions;
import com.groupdocs.editor.words.htmltowords.WordFormats;
import com.groupdocs.editor.words.htmltowords.WordsSaveOptions;
import com.groupdocs.ui.common.config.DefaultDirectories;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.LoadDocumentEntity;
import com.groupdocs.ui.common.entity.web.PageDescriptionEntity;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;
import com.groupdocs.ui.common.exception.TotalGroupDocsException;
import com.groupdocs.ui.editor.model.EditDocumentRequest;
import com.groupdocs.ui.editor.model.EditorConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

public class EditorServiceImpl implements EditorService {
    private static final Logger logger = LoggerFactory.getLogger(EditorServiceImpl.class);
    public static final Set<String> SUPPORTED_FORMATS = Sets.newHashSet("DOCX", "DOC", "DOCM", "DOTX", "ODT", "OTT", "RTF", "TXT", "HTML", "MHTML", "XML");

    public static class Format {
        byte format;
        String type;

        public Format(byte format, String type) {
            this.format = format;
            this.type = type;
        }
    }

    public static final String WORD = "Word";
    public static final String CELL = "Cell";
    public static final String PDF = "pfd";

    public static final Map<String, Format> formats = new HashMap<>();

    {
        {
            formats.put("doc", new Format(WordFormats.Doc, WORD));
            formats.put("dot", new Format(WordFormats.Dot, WORD));
            formats.put("docx", new Format(WordFormats.Docx, WORD));
            formats.put("docm", new Format(WordFormats.Docm, WORD));
            formats.put("dotx", new Format(WordFormats.Dotx, WORD));
            formats.put("dotm", new Format(WordFormats.Dotm, WORD));
            formats.put("flatOpc", new Format(WordFormats.FlatOpc, WORD));
            formats.put("rtf", new Format(WordFormats.Rtf, WORD));
            formats.put("odt", new Format(WordFormats.Odt, WORD));
            formats.put("0tt", new Format(WordFormats.Ott, WORD));
            formats.put("txt", new Format(WordFormats.Text, WORD));
            formats.put("html", new Format(WordFormats.Html, WORD));
            formats.put("mhtml", new Format(WordFormats.Mhtml, WORD));
            formats.put("wordML", new Format(WordFormats.WordML, WORD));

            formats.put("csv", new Format(CellFormats.Csv, CELL));
            formats.put("ods", new Format(CellFormats.Ods, CELL));
            formats.put("cellML", new Format(CellFormats.SpreadsheetML, CELL));
            formats.put("tabDelimited", new Format(CellFormats.TabDelimited, CELL));
            formats.put("xls", new Format(CellFormats.Xls, CELL));
            formats.put("xlsb", new Format(CellFormats.Xlsb, CELL));
            formats.put("xlsm", new Format(CellFormats.Xlsm, CELL));
            formats.put("xlsx", new Format(CellFormats.Xlsx, CELL));
            formats.put("xltm", new Format(CellFormats.Xltm, CELL));
            formats.put("xltx", new Format(CellFormats.Xltx, CELL));
        }
    }

    private GlobalConfiguration globalConfiguration;
    private EditorConfiguration editorConfiguration;

    public EditorServiceImpl(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
        this.editorConfiguration = globalConfiguration.getEditor();
        setLicense();
    }

    private void setLicense() {
        try {
            // set GroupDocs license
            License license = new License();
            license.setLicense(globalConfiguration.getApplication().getLicensePath());
        } catch (Throwable throwable) {
            logger.error("Can not verify Editor license!");
        }
    }

    @Override
    public List<FileDescriptionEntity> getFileList(String path) {
        if (StringUtils.isEmpty(path)) {
            path = editorConfiguration.getFilesDirectory();
        }
        try {
            File directory = new File(path);
            List<File> filesList = Arrays.asList(directory.listFiles());

            List<FileDescriptionEntity> fileList = getFileDescriptionEntities(filesList);
            return fileList;
        } catch (Exception ex) {
            logger.error("Exception in getting file list", ex);
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    public List<FileDescriptionEntity> getFileDescriptionEntities(List<File> filesList) {
        List<FileDescriptionEntity> fileList = new ArrayList<>();
        for (File file : filesList) {
            String guid = file.getAbsolutePath();
            String extension = FilenameUtils.getExtension(guid);
            if (file.isDirectory() || (!StringUtils.isEmpty(extension) && SUPPORTED_FORMATS.contains(extension.toUpperCase()))) {
                FileDescriptionEntity fileDescription = new FileDescriptionEntity();
                fileDescription.setGuid(guid);
                fileDescription.setName(file.getName());
                fileDescription.setDirectory(file.isDirectory());
                fileDescription.setSize(file.length());
                fileList.add(fileDescription);
            }
        }
        return fileList;
    }

    @Override
    public EditorConfiguration getEditorConfiguration() {
        return editorConfiguration;
    }

    @Override
    public LoadDocumentEntity loadDocument(LoadDocumentRequest loadDocumentRequest) {
        return loadDocumentEntity(loadDocumentRequest.getGuid());
    }

    private LoadDocumentEntity loadDocumentEntity(String guid) {
        LoadDocumentEntity doc = new LoadDocumentEntity();
        try {
            InputHtmlDocument inputHtmlDocument = EditorHandler.toHtml(new FileInputStream(guid));
            PageDescriptionEntity page = new PageDescriptionEntity();
            page.setData(inputHtmlDocument.getEmbeddedHtml());
            page.setNumber(0);
            List<PageDescriptionEntity> pages = new ArrayList<>();
            pages.add(page);
            doc.setPages(pages);
            doc.setGuid(guid);
        } catch (Exception ex) {
            logger.error("Exception in loading document");
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
        return doc;
    }

    @Override
    public Set<String> getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    @Override
    public LoadDocumentEntity saveDoc(EditDocumentRequest editDocumentRequest) {
        String guid = editDocumentRequest.getGuid();
        String filePath = !DefaultDirectories.isAbsolutePath(guid) ?
                editorConfiguration.getFilesDirectory() + File.separator + guid : guid;

        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            OutputHtmlDocument outputDocument = new OutputHtmlDocument(editDocumentRequest.getContent());
            IDocumentSaveOptions options = getSaveOptions(filePath, editDocumentRequest.getPassword());
            EditorHandler.toDocument(outputDocument, outputStream, options);
        } catch (Exception ex) {
            logger.error("Exception occurred while creating the file");
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
        return loadDocumentEntity(filePath);
    }

    private IDocumentSaveOptions getSaveOptions(String fileName, String password) {
        IDocumentSaveOptions options;
        String extension = FilenameUtils.getExtension(fileName);
        if (StringUtils.isEmpty(extension)) {
            logger.error("Not supported doc format");
            throw new IllegalArgumentException("Not supported doc format");
        }
        Format format = formats.get(extension.toLowerCase());
        switch (format.type) {
            case WORD:
                options = new WordsSaveOptions(format.format);
                break;
            case CELL:
                options = new CellsSaveOptions();
                ((CellsSaveOptions) options).setOutputFormat(format.format);
                break;
            case PDF:
                options = new PdfSaveOptions();
                break;
            default:
                logger.error("Not supported doc format");
                throw new IllegalArgumentException("Not supported doc format");
        }
        options.setPassword(password);
        return options;
    }
}