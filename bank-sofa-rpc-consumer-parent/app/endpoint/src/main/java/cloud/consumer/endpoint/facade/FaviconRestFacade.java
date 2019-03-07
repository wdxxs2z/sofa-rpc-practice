package cloud.consumer.endpoint.facade;

import cloud.consumer.endpoint.constants.RestConstants;
import cloud.consumer.endpoint.exception.CommonException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * rest interface
 * <p>
 * to fix the favicon not found bug
 * <p/>
 */
@Path("/favicon.ico")
@Consumes(RestConstants.DEFAULT_CONTENT_TYPE)
@Produces(RestConstants.DEFAULT_CONTENT_TYPE)
public interface FaviconRestFacade {

    @GET
    public String faviconIco() throws CommonException;
}
