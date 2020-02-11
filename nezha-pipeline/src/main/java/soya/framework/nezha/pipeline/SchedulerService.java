package soya.framework.nezha.pipeline;

import com.google.common.eventbus.Subscribe;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchedulerService implements ServiceEventListener<ScheduleEvent> {
    private static Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    private Scheduler scheduler;

    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public SchedulerMetaData schedulerMetaData() throws SchedulerException {
        return scheduler.getMetaData();
    }

    public JobDetail jobDetail(String name, String group) throws SchedulerException {
        return scheduler.getJobDetail(new JobKey(name, group));
    }

    public List<JobExecutionContext> currentlyExecutingJobs() throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs();
    }

    public List<String> calendarNames() throws SchedulerException {
        return scheduler.getCalendarNames();
    }

    public List<String> jobGroups() throws SchedulerException {
        return scheduler.getJobGroupNames();
    }

    public List<JobDetail> jobs(String groupName) throws SchedulerException {
        List<JobDetail> list = new ArrayList<>();
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
            list.add(scheduler.getJobDetail(jobKey));
        }
        return list;
    }

    public JobSchedule jobSchedule(String jobName, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, groupName);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        return new JobSchedule(jobDetail, triggers);
    }

    @PostConstruct
    public void init() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schedule.json");
        if(inputStream != null) {
            List<ScheduleEvent> scheduleEvents = ScheduleEventFactory.getInstance().fromJson(inputStream);

        }
    }

    @Subscribe
    public void onEvent(ScheduleEvent event) {
        try {
            scheduler.scheduleJob(event.getJobDetail(), event.getTrigger());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static class JobSchedule {
        private final JobDetail jobDetail;
        private final List<Trigger> triggers;

        private JobSchedule(JobDetail jobDetail, List<Trigger> triggers) {
            this.jobDetail = jobDetail;
            this.triggers = triggers;
        }


    }
}
