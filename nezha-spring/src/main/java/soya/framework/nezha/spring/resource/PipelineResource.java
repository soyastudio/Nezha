package soya.framework.nezha.spring.resource;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soya.framework.nezha.FunctionNode;
import soya.framework.pipeline.PipelineLogService;
import soya.framework.pipeline.PipelineServer;
import soya.framework.pipeline.PipelineService;
import soya.framework.pipeline.PipelineTriggerEvent;
import soya.framework.pipeline.deployment.PipelineDeployService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@Path("/pipeline")
@Api(value = "Pipeline Service")
public class PipelineResource {

    @Autowired
    private PipelineService pipelineService;

    @Autowired
    private PipelineLogService pipelineLogService;

    @GET
    @Path("/deployments")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deployments() {
        PipelineDeployService service = PipelineServer.getInstance().getService(PipelineDeployService.class);
        return Response.status(200).entity(service.getDeployments()).build();
    }

    @POST
    @Path("/ast")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON})
    public Response ast(String expression) {
        Gson gson = new Gson();
        FunctionNode[] functions = FunctionNode.toFunctions(expression);
        return Response.status(200).entity(gson.toJson(functions)).build();
    }

    @GET
    @Path("/deployment/{pipeline}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deployment(@PathParam("pipeline") String pipeline) {
        PipelineDeployService service = PipelineServer.getInstance().getService(PipelineDeployService.class);
        return Response.status(200).entity(service.getDeployments()).build();
    }

    @POST
    @Path("/pipeline/{pipeline}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response trigger(@PathParam("pipeline") String pipeline) {
        PipelineServer.getInstance().publish(new PipelineTriggerEvent(pipeline));
        return Response.status(200).build();
    }

    @GET
    @Path("/log/{pipeline}")
    @Produces({MediaType.TEXT_PLAIN})
    public Response log(@PathParam("pipeline") String pipeline, @QueryParam("offset") int offset,
                        @QueryParam("limit") int limit, @QueryParam("reverse") boolean reverse, @QueryParam("indexed") boolean indexed) {
        PipelineLogService service = PipelineServer.getInstance().getService(PipelineLogService.class);
        try {
            return Response.status(200).entity(service.read(pipeline, offset, limit, reverse, indexed)).build();
        } catch (IOException e) {
            return Response.status(400).build();
        }

    }
}
