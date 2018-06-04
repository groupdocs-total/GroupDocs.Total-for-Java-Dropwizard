package com.groupdocs.ui.viewer.views;

import com.groupdocs.ui.total.config.TotalConfiguration;
import io.dropwizard.views.View;
import java.nio.charset.Charset;

/**
 * Viewer View
 *
 * @author Aspose Pty Ltd
 */

public class Viewer extends View {
    private TotalConfiguration totalConfiguration;

    /**
     * Constructor
     * @param totalConfiguration total configuration
     */
    public Viewer(TotalConfiguration totalConfiguration){
        super("viewer.ftl", Charset.forName("UTF-8"));
        this.totalConfiguration = totalConfiguration;
    }

    /**
     * Get total config
     * @return total config
     */
    public TotalConfiguration getTotalConfiguration() {
        return totalConfiguration;
    }

    /**
     * Set total config
     * @param totalConfiguration total config
     */
    public void setTotalConfiguration(TotalConfiguration totalConfiguration) {
        this.totalConfiguration = totalConfiguration;
    }

}
