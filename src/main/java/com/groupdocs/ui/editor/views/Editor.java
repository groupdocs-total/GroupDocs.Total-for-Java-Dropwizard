package com.groupdocs.ui.editor.views;

import com.groupdocs.ui.common.config.GlobalConfiguration;
import io.dropwizard.views.View;

import java.nio.charset.Charset;

public class Editor extends View {
    private GlobalConfiguration globalConfiguration;

    public Editor(GlobalConfiguration globalConfiguration, String charset) {
        super("editor.ftl", Charset.forName(charset));
        this.globalConfiguration = globalConfiguration;
    }

    public GlobalConfiguration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public void setGlobalConfiguration(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }
}
