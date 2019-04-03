package cloud.provider.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("/webapi")
@Consumes("application/json;charset=UTF-8")
@Produces("application/json;charset=UTF-8")
public interface CallerService {

	@GET
	@Path("/message")
    String message();

	@GET
	@Path("/datasource")
	Map<String, Object> datasource(String name);

	@GET
	@Path("/create")
	Map<String, Object> create();

	double division(double a, double b);

	@GET
	@Path("/tracer")
	Map<String, Object> getCurrentTracerInfo();
}

