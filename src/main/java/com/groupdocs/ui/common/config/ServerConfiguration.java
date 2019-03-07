package com.groupdocs.ui.common.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;

/**
 * ServerConfiguration
 *
 * @author Aspose Pty Ltd
 */
public class ServerConfiguration extends Configuration{
    private int httpPort;

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

}
