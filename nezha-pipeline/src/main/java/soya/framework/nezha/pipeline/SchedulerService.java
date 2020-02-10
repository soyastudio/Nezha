package soya.framework.nezha.pipeline;

import com.google.common.eventbus.Subscribe;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.InputStream;
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

    public List<JobExecutionContext> currentJobs() throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs();
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
}
