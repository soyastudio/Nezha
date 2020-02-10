package soya.framework.nezha.pipeline;

import org.quartz.*;

public class ScheduleEvent extends ServiceEvent {
    private final JobDetail jobDetail;
    private final Trigger trigger;

    protected ScheduleEvent(JobDetail jobDetail, Trigger trigger) {
        super();
        this.jobDetail = jobDetail;
        this.trigger = trigger;
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

    public Trigger getTrigger() {
        return trigger;
    }
}
