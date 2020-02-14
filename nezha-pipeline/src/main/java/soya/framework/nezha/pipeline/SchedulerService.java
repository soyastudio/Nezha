package soya.framework.nezha.pipeline;

import com.google.common.eventbus.Subscribe;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SchedulerService implements ServiceEventListener<SchedulerEvent> {
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

    public List<? extends Trigger> triggers(String jobName, String groupName) throws SchedulerException {
        return scheduler.getTriggersOfJob(new JobKey(jobName, groupName));
    }

    public JobSchedule jobSchedule(String jobName, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, groupName);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        return new JobSchedule(jobDetail, triggers);
    }

    public void loadFromJson(String json) {
        System.out.println("-------------------- todo: loadFromJson");
    }

    public void loadFromYaml(String yaml) {
        System.out.println("-------------------- todo: loadFromYaml");

    }

    public void loadFromXml(String xml) {
        System.out.println("-------------------- todo: loadFromXml");
    }

    public void addJob(JobDetail jobDetail, boolean replace) throws SchedulerException {
        scheduler.addJob(jobDetail, replace);
    }

    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }

    public void pauseJobs(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {
        scheduler.pauseJobs(groupMatcher);
    }

    public void pauseJob(JobKey jobKey) throws SchedulerException {
        scheduler.pauseJob(jobKey);
    }

    public void pauseTriggers(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {
        scheduler.pauseTriggers(groupMatcher);
    }

    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {
        scheduler.pauseTrigger(triggerKey);
    }

    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }


    public boolean unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.unscheduleJob(triggerKey);
    }

    public Date rescheduleJob(TriggerKey triggerKey, Trigger trigger) throws SchedulerException {
        return scheduler.rescheduleJob(triggerKey, trigger);
    }

    public boolean deleteJob(JobKey jobKey) throws SchedulerException {
        return scheduler.deleteJob(jobKey);
    }

    public boolean deleteJobs(List<JobKey> list) throws SchedulerException {
        return scheduler.deleteJobs(list);
    }

    public void resumeJob(JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);
    }

    public void resumeJobs(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {
        scheduler.resumeJobs(groupMatcher);
    }

    public void resumeTrigger(TriggerKey triggerKey) throws SchedulerException {
        scheduler.resumeTrigger(triggerKey);
    }

    public void resumeTriggers(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {
        scheduler.resumeTriggers(groupMatcher);
    }

    public Set<JobKey> findJobs(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {
        return scheduler.getJobKeys(groupMatcher);
    }

    public Set<TriggerKey> findTriggers(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {
        return scheduler.getTriggerKeys(groupMatcher);
    }

    @Subscribe
    public void onEvent(SchedulerEvent event) {
        if (event instanceof ScheduleEvent) {
            doSchedule((ScheduleEvent) event);
        }
    }

    private void doSchedule(ScheduleEvent event) {
        try {
            JobDetail jobDetail = event.getJobDetail();
            for (Trigger trigger : event.getTriggers()) {
                scheduler.scheduleJob(event.getJobDetail(), trigger);
            }
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
