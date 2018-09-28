package com.groupdocs.ui.total.resources;

import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.resources.Resources;
import com.groupdocs.ui.total.views.Total;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Total Resources
 *
 * @author Aspose Pty Ltd
 */

@Path(value = "/")
public class TotalResources extends Resources {

    @Override
    protected String getStoragePath(Map<String, Object> params) {
        return "";
    }

    /**
     * Constructor
     * @param globalConfiguration global configuration object
     * @throws UnknownHostException
     */
    public TotalResources(GlobalConfiguration globalConfiguration) throws UnknownHostException {
        super(globalConfiguration);
    }

    /**
     * Get and set total page
     * @return html view
     */
    @GET
    public Total getView(){
        return new Total(DEFAULT_CHARSET);
    }
}
