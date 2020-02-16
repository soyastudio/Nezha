package soya.framework.nezha.spring.resource;

import io.swagger.annotations.Api;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soya.framework.pipeline.scheduler.SchedulerService;
import soya.framework.nezha.spring.model.JobDetailModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

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
    @Path("/load/xml")
    @Consumes({MediaType.APPLICATION_XML})
    public Response deployFromXml(String xml) {
        schedulerService.loadFromXml(xml);
        return Response.status(200).build();
    }

    @POST
    @Path("/load/json")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response deployFromJson(String json) {
        schedulerService.loadFromJson(json);
        return Response.status(200).build();
    }

    @POST
    @Path("load/yaml")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response deployFromYaml(String yaml) {
        schedulerService.loadFromYaml(yaml);
        return Response.status(200).build();
    }

    // =============================================== Job
    @POST
    @Path("/job")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addJob(JobDetailModel jobDetailModel) {
        try {
            schedulerService.addJob(jobDetailModel.toJobDetail(), false);
            return Response.status(200).build();

        } catch (ClassNotFoundException | SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/job")
    @Produces({MediaType.APPLICATION_JSON})
    public Response jobDetail(@QueryParam("jobName") String jobName, @QueryParam("jobGroup") String jobGroup) {
        try {
            Object result = null;
            if (jobName != null) {
                result = schedulerService.jobDetail(jobName, jobGroup);
            } else if (jobGroup != null) {
                result = schedulerService.jobs(jobGroup);

            } else {

            }

            return Response.status(200).entity(result).build();

        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/job/groups")
    @Produces({MediaType.APPLICATION_JSON})
    public Response jobGroups() {
        try {
            return Response.status(200).entity(schedulerService.jobGroups()).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @PUT
    @Path("/job")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addOrReplaceJob(JobDetailModel jobDetailModel) {
        try {
            schedulerService.addJob(jobDetailModel.toJobDetail(), true);
            return Response.status(200).build();

        } catch (ClassNotFoundException | SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @PUT
    @Path("/job/pause")
    public Response pauseJob(@HeaderParam("jobName") String jobName, @HeaderParam("jobGroup") String jobGroup) {
        try {
            if (jobName != null) {
                schedulerService.pauseJob(new JobKey(jobName, jobGroup));

            } else if (jobGroup != null) {
                schedulerService.pauseJobs(GroupMatcher.jobGroupEquals(jobGroup));

            } else {
                schedulerService.pauseAll();
            }
            return Response.status(200).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @PUT
    @Path("/job/resume")
    public Response resumeJob(@HeaderParam("jobName") String jobName, @HeaderParam("jobGroup") String jobGroup) {
        try {
            if (jobName != null) {
                schedulerService.resumeJob(new JobKey(jobName, jobGroup));
            } else if (jobGroup != null) {
                schedulerService.resumeJobs(GroupMatcher.jobGroupEquals(jobGroup));
            } else {
                schedulerService.resumeAll();
            }
            return Response.status(200).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @DELETE
    @Path("/job")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteJob(@HeaderParam("jobName") String jobName, @HeaderParam("jobGroup") String jobGroup) {
        try {
            if (jobName != null) {
                schedulerService.deleteJob(new JobKey(jobName, jobGroup));
            } else if (jobGroup != null) {
                schedulerService.deleteJobs(new ArrayList<>(schedulerService.findJobs(GroupMatcher.jobGroupEquals(jobGroup))));
            }
            return Response.status(200).build();

        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    // =============================================== Trigger
    @GET
    @Path("/job/trigger")
    @Produces({MediaType.APPLICATION_JSON})
    public Response triggers(@QueryParam("jobName") String jobName, @QueryParam("jobGroup") String jobGroup, @QueryParam("triggerGroup") String triggerGroup) {
        try {
            return Response.status(200).entity(schedulerService.triggers(jobName, jobGroup)).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @PUT
    @Path("/job/trigger/pause")
    public Response pauseTriggers(@QueryParam("triggerName") String triggerName, @QueryParam("triggerGroup") String triggerGroup) {
        try {
            if (triggerName != null) {
                schedulerService.pauseTrigger(new TriggerKey(triggerName, triggerGroup));
            } else if (triggerGroup != null) {
                schedulerService.pauseTriggers(GroupMatcher.triggerGroupEquals(triggerGroup));

            } else {

            }
            return Response.status(200).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

    @PUT
    @Path("/job/trigger/resume")
    public Response resumeTriggers(@QueryParam("triggerName") String triggerName, @QueryParam("triggerGroup") String triggerGroup) {
        try {
            if (triggerName != null) {
                schedulerService.resumeTrigger(new TriggerKey(triggerName, triggerGroup));
            } else if (triggerGroup != null) {
                schedulerService.resumeTriggers(GroupMatcher.triggerGroupEquals(triggerGroup));
            }
            return Response.status(200).build();
        } catch (SchedulerException e) {
            return Response.status(500).build();
        }
    }

}
