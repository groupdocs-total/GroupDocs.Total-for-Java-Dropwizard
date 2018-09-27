package com.groupdocs.ui.comparison.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupdocs.ui.common.config.CommonConfiguration;

import javax.validation.Valid;

public class ComparisonConfiguration extends CommonConfiguration {

    @Valid
    @JsonProperty
    private String filesDirectory;

    @Valid
    @JsonProperty
    private String resultDirectory;

    @Valid
    @JsonProperty
    private Integer preloadResultPageCount;

    @Valid
    @JsonProperty
    private Boolean multiComparing;

    public String getFilesDirectory() {
        return filesDirectory;
    }

    public void setFilesDirectory(String filesDirectory) {
        this.filesDirectory = filesDirectory;
    }

    public String getResultDirectory() {
        return resultDirectory;
    }

    public void setResultDirectory(String resultDirectory) {
        this.resultDirectory = resultDirectory;
    }

    public Integer getPreloadResultPageCount() {
        return preloadResultPageCount;
    }

    public void setPreloadResultPageCount(Integer preloadResultPageCount) {
        this.preloadResultPageCount = preloadResultPageCount;
    }

    public Boolean getMultiComparing() {
        return multiComparing;
    }

    public void setMultiComparing(Boolean multiComparing) {
        this.multiComparing = multiComparing;
    }

    @Override
    public String toString() {
        return super.toString() +
                "ComparisonConfiguration{" +
                "filesDirectory='" + filesDirectory + '\'' +
                ", resultDirectory='" + resultDirectory + '\'' +
                ", preloadResultPageCount=" + preloadResultPageCount +
                ", multiComparing=" + multiComparing +
                '}';
    }
}
