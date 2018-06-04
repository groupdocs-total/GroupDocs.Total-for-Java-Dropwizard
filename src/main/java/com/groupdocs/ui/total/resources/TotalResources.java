package com.groupdocs.ui.total.resources;

import com.groupdocs.ui.total.views.Total;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Total Resources
 *
 * @author Aspose Pty Ltd
 */

@Path(value = "/")
public class TotalResources extends Resources {

    /**
     * Get and set total page
     * @return html view
     */
    @GET
    public Total getView(){
        return new Total();
    }
}
