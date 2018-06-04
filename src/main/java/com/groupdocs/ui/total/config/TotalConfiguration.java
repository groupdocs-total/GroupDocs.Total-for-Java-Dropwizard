package com.groupdocs.ui.total.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupdocs.ui.common.config.ApplicationConfiguration;
import com.groupdocs.ui.common.config.ServerConfiguration;
import com.groupdocs.ui.viewer.config.ViewerConfiguration;
import io.dropwizard.Configuration;

import javax.validation.Valid;

/**
 * TotalConfiguration
 * Object to hold all application's configurations from yml file
 *
 * @author Aspose Pty Ltd
 */
public class TotalConfiguration extends Configuration{

    @Valid
    @JsonProperty
    private ServerConfiguration server;

    @Valid
    @JsonProperty
    private ApplicationConfiguration application;

    @Valid
    @JsonProperty
    private ViewerConfiguration viewer;

    /**
     * Constructor
     */
    public TotalConfiguration(){
        server = new ServerConfiguration();
        application = new ApplicationConfiguration();
        viewer = new ViewerConfiguration();
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
     * Get viewer configuration
     * @return viewer configuration
     */
    public ViewerConfiguration getViewer() {
        return viewer;
    }

}


