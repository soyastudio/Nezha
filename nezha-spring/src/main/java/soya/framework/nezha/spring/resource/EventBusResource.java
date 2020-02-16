package soya.framework.nezha.spring.resource;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import soya.framework.pipeline.PipelineServer;
import soya.framework.pipeline.PipelineTriggerEvent;
import soya.framework.pipeline.ServiceEvent;
import soya.framework.nezha.spring.model.EventModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Path("/event")
@Api(value = "Event Service")
public class EventBusResource {

    @GET
    @Path("/type")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eventTypes() {
        List<EventModel> events = new ArrayList<>();
        Set<Class<? extends ServiceEvent>> set = PipelineServer.getInstance().eventTypes();
        set.forEach(e -> {
            events.add(new EventModel(e, new ArrayList<>(PipelineServer.getInstance().listeners(e))));
        });

        return Response.status(200).entity(events).build();
    }

    @POST
    @Path("/pipeline/{pipeline}")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response publish(@PathParam("pipeline") String pipeline, String data) {
        if (data == null) {
            PipelineServer.getInstance().publish(new PipelineTriggerEvent(pipeline));
        } else {

        }

        return Response.status(200).build();
    }
}
