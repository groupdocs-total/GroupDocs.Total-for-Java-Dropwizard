package com.groupdocs.ui.total.views;

import com.groupdocs.ui.total.config.TotalConfig;
import io.dropwizard.views.View;
import java.nio.charset.Charset;

/**
 * Viewer View
 *
 * @author Aspose Pty Ltd
 */

public class Viewer extends View {
    private TotalConfig totalConfig;

    /**
     * Constructor
     * @param totalConfig total configuration
     */
    public Viewer(TotalConfig totalConfig){
        super("viewer.ftl", Charset.forName("UTF-8"));
        this.totalConfig = totalConfig;
    }

    /**
     * Get total config
     * @return total config
     */
    public TotalConfig getTotalConfig() {
        return totalConfig;
    }

    /**
     * Set total config
     * @param totalConfig total config
     */
    public void setTotalConfig(TotalConfig totalConfig) {
        this.totalConfig = totalConfig;
    }

}
