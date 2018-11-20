package com.groupdocs.ui.common.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupdocs.ui.comparison.config.ComparisonConfiguration;
import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.viewer.config.ViewerConfiguration;
import com.groupdocs.ui.annotation.config.AnnotationConfiguration;
import io.dropwizard.Configuration;

import javax.validation.Valid;

/**
 * GlobalConfiguration
 * Object to hold all application's configurations from yml file
 *
 * @author Aspose Pty Ltd
 */
public class GlobalConfiguration extends Configuration {

    @Valid
    @JsonProperty
    private ServerConfiguration server;

    @Valid
    @JsonProperty
    private ApplicationConfiguration application;

    @Valid
    @JsonProperty
    private CommonConfiguration common;

    @Valid
    @JsonProperty
    private ViewerConfiguration viewer;

    @Valid
    @JsonProperty
    private SignatureConfiguration signature;

    @Valid
    @JsonProperty
    private AnnotationConfiguration annotation;

    @Valid
    @JsonProperty
    private ComparisonConfiguration comparison;

    /**
     * Constructor
     */
    public GlobalConfiguration(){
        server = new ServerConfiguration();
        application = new ApplicationConfiguration();
        common = new CommonConfiguration();
        viewer = new ViewerConfiguration();
        signature = new SignatureConfiguration();
        annotation = new AnnotationConfiguration();
        comparison = new ComparisonConfiguration();
    }

    /**
     * Get server configuration
     * @return server configuration
     */
    public ServerConfiguration getServer() {
        return server;
    }

    /**
     * Get application configuration
     * @return application configuration
     */
    public ApplicationConfiguration getApplication() {
        return application;
    }

    /**
     * Get common configuration
     * @return common configuration
     */
    public CommonConfiguration getCommon() {
        return common;
    }

    /**
     * Get viewer configuration
     * @return viewer configuration
     */
    public ViewerConfiguration getViewer() {
        return viewer;
    }

    /**
     * Get signature configuration
     * @return signature configuration
     */
    public SignatureConfiguration getSignature() {
        return signature;
    }

    /**
     * Get annotation configuration
     * @return annotation configuration
     */
    public AnnotationConfiguration getAnnotation() {
        return annotation;
    }

    /**
     * Get comparison configuration
     * @return comparison configuration
     */
    public ComparisonConfiguration getComparison() {
        return comparison;
    }
}


