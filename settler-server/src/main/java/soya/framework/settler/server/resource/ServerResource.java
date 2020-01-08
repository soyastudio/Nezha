package soya.framework.settler.server.resource;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import soya.framework.settler.EvaluateEngine;
import soya.framework.settler.EvaluateFunction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/settler")
@Api(value = "Settler Service")
public class ServerResource {
    @POST
    @Path("/ast")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON})
    public Response ast(String expression) {
        Gson gson = new Gson();
        EvaluateFunction[] functions = EvaluateFunction.toFunctions(expression);
        return Response.status(200).entity(gson.toJson(functions)).build();
    }

    @POST
    @Path("/evaluate")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response evaluate(@HeaderParam("expression") String expression, String json) {
        EvaluateEngine engine = EvaluateEngine.getInstance();
        return Response.status(200).entity(engine.evaluate(json, expression)).build();
    }
}
