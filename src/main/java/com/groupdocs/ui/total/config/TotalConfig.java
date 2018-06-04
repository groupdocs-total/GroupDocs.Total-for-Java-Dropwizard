package com.groupdocs.ui.total.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;

/**
 * TotalConfig
 * Object to hold all application's configurations from yml file
 *
 * @author Aspose Pty Ltd
 */
public class TotalConfig extends Configuration{

    private Server server;

    @Valid
    @JsonProperty
    private Application application;

    @Valid
    @JsonProperty
    private Viewer viewer;

    /**
     * Constructor
     */
    public TotalConfig(){
        server = new Server();
        application = new Application();
        viewer = new Viewer();
    }

    /**
     * Get server configuration
     * @return server configuration
     */
    public Server getServer() {
        return server;
    }

    /**
     * Get application configuration
     * @return application configuration
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Get viewer configuration
     * @return viewer configuration
     */
    public Viewer getViewer() {
        return viewer;
    }

    /**
     * Server configuration
     */
    public class Server{
        private int httpPort;
        private String hostAddress;

        public int getHttpPort() {
            return httpPort;
        }

        public void setHttpPort(int httpPort) {
            this.httpPort = httpPort;
        }

        public String getHostAddress() {
            return hostAddress;
        }

        public void setHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
        }
    }

    /**
     * Application configuration
     */
    public class Application{

        @Valid
        @JsonProperty
        private String licensePath;

        public String getLicensePath() {
            return licensePath;
        }

        public void setLicensePath(String licensePath) {
            this.licensePath = licensePath;
        }

    }

    /**
     * Viewer configuration
     */
    public class Viewer{

        @Valid
        @JsonProperty
        private String filesDirectory;

        @Valid
        @JsonProperty
        private String fontsDirectory;

        @Valid
        @JsonProperty
        private int preloadPageCount;

        @Valid
        @JsonProperty
        private boolean zoom;

        @Valid
        @JsonProperty
        private boolean pageSelector;

        @Valid
        @JsonProperty
        private boolean search;

        @Valid
        @JsonProperty
        private boolean thumbnails;

        @Valid
        @JsonProperty
        private boolean rotate;

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
        private String defaultDocument;

        @Valid
        @JsonProperty
        private boolean browse;

        @Valid
        @JsonProperty
        private boolean htmlMode;

        @Valid
        @JsonProperty
        private boolean rewrite;

        public String getFilesDirectory() {
            return filesDirectory;
        }

        public void setFilesDirectory(String filesDirectory) {
            this.filesDirectory = filesDirectory;
        }

        public String getFontsDirectory() {
            return fontsDirectory;
        }

        public void setFontsDirectory(String fontsDirectory) {
            this.fontsDirectory = fontsDirectory;
        }

        public int getPreloadPageCount() {
            return preloadPageCount;
        }

        public void setPreloadPageCount(int preloadPageCount) {
            this.preloadPageCount = preloadPageCount;
        }

        public boolean isZoom() {
            return zoom;
        }

        public void setZoom(boolean zoom) {
            this.zoom = zoom;
        }

        public boolean isPageSelector() {
            return pageSelector;
        }

        public void setPageSelector(boolean pageSelector) {
            this.pageSelector = pageSelector;
        }

        public boolean isSearch() {
            return search;
        }

        public void setSearch(boolean search) {
            this.search = search;
        }

        public boolean isThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(boolean thumbnails) {
            this.thumbnails = thumbnails;
        }

        public boolean isRotate() {
            return rotate;
        }

        public void setRotate(boolean rotate) {
            this.rotate = rotate;
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

        public String getDefaultDocument() {
            return defaultDocument;
        }

        public void setDefaultDocument(String defaultDocument) {
            this.defaultDocument = defaultDocument;
        }

        public boolean isBrowse() {
            return browse;
        }

        public void setBrowse(boolean browse) {
            this.browse = browse;
        }

        public boolean isHtmlMode() {
            return htmlMode;
        }

        public void setHtmlMode(boolean htmlMode) {
            this.htmlMode = htmlMode;
        }

        public boolean isRewrite() {
            return rewrite;
        }

        public void setRewrite(boolean rewrite) {
            this.rewrite = rewrite;
        }

    }

}


