package soya.framework.nezha.spring.resource;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soya.framework.pipeline.deployment.PipelineDeployEvent;
import soya.framework.pipeline.deployment.PipelineDeployService;
import soya.framework.pipeline.PipelineServer;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Path("/deploy")
@Api(value = "Deploy Service")
public class DeployResource {

    @Autowired
    private PipelineDeployService deployService;

    @GET
    @Path("/deployment")
    public Response deployment(@QueryParam("pipeline") @Nullable String pipeline) {
        Object result = null;
        if(pipeline != null) {
            result = deployService.getDeployment(pipeline);
        } else {
            result = deployService.getDeployments();
        }

        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/deployment/{pipeline}")
    public Response deploy(@PathParam("pipeline") String pipeline) {
        return Response.status(200).build();
    }

    @PUT
    @Path("/deployment/{pipeline}")
    public Response redeploy(@PathParam("pipeline") String pipeline) {
        return Response.status(200).build();
    }

    @DELETE
    @Path("/deployment/{pipeline}")
    public Response undeploy(@PathParam("pipeline") String pipeline) {
        PipelineServer.getInstance().publish(new PipelineDeployEvent(pipeline, PipelineDeployEvent.DeployEventType.UNDEPLOY));
        return Response.status(200).build();
    }
}
