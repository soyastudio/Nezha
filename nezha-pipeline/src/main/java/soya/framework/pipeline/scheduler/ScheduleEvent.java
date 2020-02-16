package soya.framework.pipeline.scheduler;

import org.quartz.*;

import java.util.ArrayList;
import java.util.List;

public class ScheduleEvent extends SchedulerEvent {
    private final JobDetail jobDetail;
    private final List<Trigger> triggers = new ArrayList<>();

    protected ScheduleEvent(JobDetail jobDetail, Trigger trigger) {
        super();
        this.jobDetail = jobDetail;
        this.triggers.add(trigger);
    }

    protected ScheduleEvent(JobDetail jobDetail, List<Trigger> triggers) {
        super();
        this.jobDetail = jobDetail;
        this.triggers.addAll(triggers);
    }

    public ScheduleEvent(JobDetail jobDetail, int seconds) {
        this(jobDetail,
                TriggerBuilder.newTrigger().forJob(jobDetail)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(seconds)).build());
    }

    public ScheduleEvent(JobDetail jobDetail, int seconds, int count) {
        this(jobDetail,
                TriggerBuilder.newTrigger().forJob(jobDetail)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(count, seconds)).build());
    }

    public ScheduleEvent(JobDetail jobDetail, String cronExpression) {
        this(jobDetail,
                TriggerBuilder.newTrigger().forJob(jobDetail)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build());
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }
}
