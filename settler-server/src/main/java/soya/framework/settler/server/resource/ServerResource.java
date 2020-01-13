package soya.framework.settler.server.resource;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import soya.framework.settler.EvaluateEngine;
import soya.framework.settler.EvaluateFunction;
import soya.framework.settler.server.server.PipelineDeploymentService;
import soya.framework.settler.server.server.PipelineLogService;
import soya.framework.settler.server.server.PipelineTriggerEvent;
import soya.framework.settler.server.server.Server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Component
@Path("/console")
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

    @GET
    @Path("/deployments")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deployments() {
        PipelineDeploymentService service = Server.getInstance().getService(PipelineDeploymentService.class);
        return Response.status(200).entity(service.getDeployments()).build();
    }

    @GET
    @Path("/deployment/{pipeline}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deployment(@PathParam("pipeline") String pipeline) {
        PipelineDeploymentService service = Server.getInstance().getService(PipelineDeploymentService.class);
        return Response.status(200).entity(service.getDeployments()).build();
    }



    @POST
    @Path("/pipeline/{pipeline}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response trigger(@PathParam("pipeline") String pipeline) {
        Server.getInstance().publish(new PipelineTriggerEvent(pipeline));
        return Response.status(200).build();
    }

    @GET
    @Path("/log/{pipeline}")
    @Produces({MediaType.TEXT_PLAIN})
    public Response log(@PathParam("pipeline") String pipeline, @QueryParam("offset") int offset,
                        @QueryParam("limit") int limit, @QueryParam("reverse") boolean reverse, @QueryParam("indexed") boolean indexed) {
        PipelineLogService service = Server.getInstance().getService(PipelineLogService.class);
        try {
            return Response.status(200).entity(service.read(pipeline, offset, limit, reverse, indexed)).build();
        } catch (IOException e) {
            return Response.status(400).build();
        }

    }
}
