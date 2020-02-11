package soya.framework.nezha.spring.resource;

import io.swagger.annotations.Api;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soya.framework.nezha.pipeline.SchedulerService;

import javax.ws.rs.*;
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
    @Path("/calendars")
    @Produces({MediaType.APPLICATION_JSON})
    public Response calendars() {
        try {
            return Response.status(200).entity(schedulerService.calendarNames()).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/job-groups")
    @Produces({MediaType.APPLICATION_JSON})
    public Response jobGroups() {
        try {
            return Response.status(200).entity(schedulerService.jobGroups()).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/jobs/{groupName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response jobSchedules(@PathParam("groupName") String groupName) {
        try {
            return Response.status(200).entity(schedulerService.jobs(groupName)).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/job-schedule/{jobName}/{groupName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response jobSchedules(@PathParam("jobName") String jobName, @PathParam("groupName") String groupName) {
        try {
            return Response.status(200).entity(schedulerService.jobSchedule(jobName, groupName)).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/currently-executing-jobs")
    @Produces({MediaType.APPLICATION_JSON})
    public Response currentJobs() {
        try {
            return Response.status(200).entity(schedulerService.currentlyExecutingJobs()).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @POST
    @Path("deploy/xml")
    @Consumes({MediaType.APPLICATION_XML})
    public Response deployFromXml(String xml) {
        return Response.status(200).build();
    }

    @POST
    @Path("deploy/json")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response deployFromJson(String json) {
        return Response.status(200).build();
    }

    @POST
    @Path("deploy/yaml")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response deployFromYaml(String yaml) {
        return Response.status(200).build();
    }

}
