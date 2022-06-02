package ar.edu.itba.paw.webapp.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/")
public class RootController {
@Context
    private UriInfo uriInfo;

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response listEndpoints() {
        return Response.temporaryRedirect(uriInfo.getBaseUriBuilder().path("application.wadl").build()).build();
    }
}


