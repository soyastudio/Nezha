package soya.framework.settler.server.server;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import soya.framework.settler.ProcessSession;
import soya.framework.settler.Workflow;
import soya.framework.settler.WorkflowEngine;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class PipelineService implements ServiceEventListener<PipelineEvent> {
    static final Logger logger = LoggerFactory.getLogger(PipelineService.class);

    private Map<String, Pipeline> pipelines = new ConcurrentHashMap<>();
    private Map<String, PipelineEvent> currentEvents = new ConcurrentHashMap<>();

    private PipelineFactory pipelineFactory;
    private Scheduler scheduler;

    public PipelineService(Scheduler scheduler) {
        this.pipelineFactory = new DefaultPipelineFactory();
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void init() {
        new Timer("Pipeline Event Ack Timer").schedule(new AckTask(this), 1000L, 15000L);
    }

    @Subscribe
    public void onEvent(PipelineEvent pipelineEvent) {
        if (pipelineEvent instanceof PipelineAckEvent) {
            currentEvents.remove(pipelineEvent.getPipeline());

        } else if (currentEvents.containsKey(pipelineEvent.getPipeline())) {
            while (currentEvents.containsKey(pipelineEvent.getPipeline())) {
                PipelineEvent processing = currentEvents.get(pipelineEvent.getPipeline());
                long timeToWait = processing.getCreatedTime() + processing.getTimeout() - System.currentTimeMillis();
                if (timeToWait < 0) {

                } else {
                    try {
                        Thread.sleep(timeToWait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        } else {
            handle(pipelineEvent);
        }
    }

    protected void handle(PipelineEvent event) {
        currentEvents.put(event.getPipeline(), event);
        if (event instanceof PipelineScheduleEvent) {
            PipelineScheduleEvent scheduleEvent = (PipelineScheduleEvent) event;
            try {
                Pipeline pipeline = pipelineFactory.create(scheduleEvent.getDir());
                pipelines.put(event.getPipeline(), pipeline);
                schedulePipeline(pipeline);

            } catch (Exception e) {
                // TODO:
                throw new RuntimeException(e);
            }

        } else if (event instanceof PipelineTriggerEvent) {
            if (pipelines.containsKey(event.getPipeline())) {
                triggerPipeline(pipelines.get(event.getPipeline()));

            } else {
                // TODO:
            }
        }
    }

    protected void triggerPipeline(Pipeline pipeline) {
        logger.info("triggering pipeline: {}", pipeline.getName());

        ((Runnable) () -> {
            Future<ProcessSession> future = WorkflowEngine.getInstance().execute(pipeline);
            while (!future.isDone()) {
                try {
                    Thread.sleep(100l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ack(pipeline);
        }).run();

    }

    protected void schedulePipeline(Pipeline pipeline) {
        logger.info("scheduling pipeline: {}", pipeline.getName());
        try {
            if (pipeline.getConfiguration().getScheduler() != null) {
                JobDetail jobDetail = JobBuilder.newJob(PipelineJob.class)
                        .withIdentity(pipeline.getName(), "pipeline")
                        .build();

                int interval = pipeline.getConfiguration().getScheduler().getInterval();
                int delay = pipeline.getConfiguration().getScheduler().getDelay();
                String calendar = pipeline.getConfiguration().getScheduler().getCalendar();

                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(pipeline.getName(), "pipeline")
                        .startAt(new Date(System.currentTimeMillis() + delay * 1000))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(interval)
                                .repeatForever())
                        .build();

                scheduler.scheduleJob(jobDetail, trigger);
            }

        } catch (SchedulerException e) {
            throw new RuntimeException(e);

        } finally {
            ack(pipeline);
        }
    }

    protected void ack(Pipeline pipeline) {
        PipelineServer.getInstance().publish(new PipelineAckEvent(pipeline.getName()));
    }

    static class PipelineJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            String pipeline = jobExecutionContext.getJobDetail().getKey().getName();
            PipelineServer.getInstance().publish(new PipelineTriggerEvent(pipeline));
        }
    }

    static class AckTask extends TimerTask {
        private PipelineService pipelineService;

        private AckTask(PipelineService pipelineService) {
            this.pipelineService = pipelineService;
        }

        @Override
        public void run() {
            Iterator<PipelineEvent> iterator = pipelineService.currentEvents.values().iterator();
            while (iterator.hasNext()) {
                PipelineEvent event = iterator.next();
                if (System.currentTimeMillis() - event.getCreatedTime() > event.getTimeout()) {
                    PipelineServer.getInstance().publish(new PipelineAckEvent(event.getPipeline()));
                }
            }
        }
    }

    static class DefaultPipelineFactory implements PipelineFactory {

        @Override
        public Pipeline create(File base) throws PipelineCreateException {
            File pipelineYaml = new File(base, "pipeline.yaml");
            try {
                Pipeline pipeline = Pipeline.fromYaml(base, new FileInputStream(pipelineYaml));
                pipeline.init(PipelineServer.getInstance());
                return pipeline;

            } catch (FileNotFoundException e) {
                throw new PipelineCreateException(e);
            }
        }
    }
}
