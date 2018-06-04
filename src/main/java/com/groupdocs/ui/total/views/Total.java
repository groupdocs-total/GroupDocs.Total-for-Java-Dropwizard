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
     * @param charset charset
     */
    public Total(String charset) {
        super("total.ftl", Charset.forName(charset));
    }

}
