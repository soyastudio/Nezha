package soya.framework.settler.server.resource;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import soya.framework.settler.server.server.PipelineDeployEvent;
import soya.framework.settler.server.server.PipelineServer;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Path("/deploy")
@Api(value = "Deploy Service")
public class DeployResource {

    @GET
    @Path("/deployments")
    public Response deployments() {
        return Response.status(200).build();
    }

    @GET
    @Path("/{pipeline}")
    public Response deployment(@PathParam("pipeline") String pipeline) {
        return Response.status(200).build();
    }

    @POST
    @Path("/{pipeline}")
    public Response deploy(@PathParam("pipeline") String pipeline) {
        return Response.status(200).build();
    }

    @PUT
    @Path("/{pipeline}")
    public Response redeploy(@PathParam("pipeline") String pipeline) {
        return Response.status(200).build();
    }

    @DELETE
    @Path("/{pipeline}")
    public Response undeploy(@PathParam("pipeline") String pipeline) {
        PipelineServer.getInstance().publish(new PipelineDeployEvent(pipeline, PipelineDeployEvent.DeployEventType.UNDEPLOY));
        return Response.status(200).build();
    }
}
