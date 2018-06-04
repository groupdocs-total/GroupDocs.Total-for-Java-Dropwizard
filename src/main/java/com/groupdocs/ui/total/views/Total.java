package com.groupdocs.ui.total.views;

import io.dropwizard.views.View;
import java.nio.charset.Charset;

/**
 * Total View
 *
 * @author Aspose Pty Ltd
 */

public class Total extends View {

    /**
     * Constructor
     */
    public Total() {
        super("total.ftl", Charset.forName("UTF-8"));
    }

}
