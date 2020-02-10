package soya.framework.nezha.spring.resource;

import io.swagger.annotations.Api;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soya.framework.nezha.pipeline.SchedulerService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/scheduler")
@Api(value = "Scheduler Service")
public class SchedulerResource {

    @Autowired
    private SchedulerService schedulerService;

    @GET
    @Path("/metadata")
    @Produces({MediaType.APPLICATION_JSON})
    public Response schedulerInformation() {
        try {
            SchedulerMetaData metaData = schedulerService.schedulerMetaData();
            return Response.status(200).entity(metaData).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/jobs")
    @Produces({MediaType.APPLICATION_JSON})
    public Response currentJobs() {
        try {
            return Response.status(200).entity(schedulerService.currentJobs()).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

}
