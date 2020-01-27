package soya.framework.settler.server.resource;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import soya.framework.settler.server.server.PipelineServer;
import soya.framework.settler.server.server.PipelineTriggerEvent;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/publish")
@Api(value = "Event Bus Service")
public class EventBusResource {

    @POST
    @Path("/pipeline/{pipeline}")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response publish(@PathParam("pipeline") String pipeline, String data) {
        if(data == null) {
            PipelineServer.getInstance().publish(new PipelineTriggerEvent(pipeline));
        } else {

        }

        return Response.status(200).build();
    }
}
