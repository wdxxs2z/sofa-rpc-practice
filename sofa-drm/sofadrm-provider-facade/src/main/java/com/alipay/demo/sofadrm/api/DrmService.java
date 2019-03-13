package com.alipay.demo.sofadrm.api;

import javax.ws.rs.*;

@Path("/webapi")
@Consumes("application/json;charset=UTF-8")
@Produces("application/json;charset=UTF-8")
public interface DrmService {

    @GET
    @Path("/info/{message}")
    public String testMessage(@PathParam("message") String message);
}
