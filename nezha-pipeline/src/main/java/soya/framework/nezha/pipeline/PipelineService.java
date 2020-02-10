package soya.framework.nezha.pipeline;

import com.google.common.eventbus.Subscribe;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soya.framework.nezha.ProcessSession;
import soya.framework.nezha.WorkflowCallback;
import soya.framework.nezha.support.SingletonWorkflowEngine;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineService implements ServiceEventListener<PipelineEvent> {
    static final Logger logger = LoggerFactory.getLogger(PipelineService.class);

    private Map<String, Pipeline> pipelines = new ConcurrentHashMap<>();
    private Map<String, PipelineEvent> currentEvents = new ConcurrentHashMap<>();

    private PipelineFactory pipelineFactory;

    public PipelineService() {
        this.pipelineFactory = new DefaultPipelineFactory();
    }

    @PostConstruct
    public void init() {
        new Timer("Pipeline Event Ack Timer").schedule(new AckTask(this), 1000L, 15000L);
    }

    @Subscribe
    public void onEvent(PipelineEvent pipelineEvent) {
        if (pipelineEvent instanceof PipelineInitializationEvent) {
            logger.info("Initializing pipeline: {}", pipelineEvent.getPipeline());
            PipelineInitializationEvent event = (PipelineInitializationEvent) pipelineEvent;
            try {
                Pipeline pipeline = pipelineFactory.create(event.getDir());
                pipelines.put(pipeline.getName(), pipeline);

                if (pipeline.getConfiguration().getScheduler() != null) {
                    JobDetail jobDetail = JobBuilder.newJob(PipelineService.PipelineJob.class)
                            .withIdentity(pipeline.getName(), "pipeline")
                            .build();


                    int interval = pipeline.getConfiguration().getScheduler().getInterval();
                    int delay = pipeline.getConfiguration().getScheduler().getDelay();

                    String calendar = pipeline.getConfiguration().getScheduler().getCalendar();
                    if(calendar != null) {
                        PipelineServer.getInstance().publish(new ScheduleEvent(jobDetail, calendar));
                    } else {
                        PipelineServer.getInstance().publish(new ScheduleEvent(jobDetail, interval));
                    }
                }

            } catch (PipelineCreateException e) {
                //TODO:
                e.printStackTrace();
            }

        } else if (pipelineEvent instanceof PipelineAckEvent) {
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
        if (!pipelines.containsKey(event.getPipeline())) {
            logger.warn("Pipeline not defined: {}", event.getPipeline());
            return;
        }

        currentEvents.put(event.getPipeline(), event);
        if (event instanceof PipelineTriggerEvent) {
            if (pipelines.containsKey(event.getPipeline())) {
                triggerPipeline(pipelines.get(event.getPipeline()));

            } else {
                // TODO:
            }
        }
    }

    protected void triggerPipeline(Pipeline pipeline) {
        logger.info("triggering pipeline: {}", pipeline.getName());

        SingletonWorkflowEngine.getInstance().execute(pipeline, new WorkflowCallback() {
            @Override
            public void onSuccess(ProcessSession session) {
                ack(pipeline);
            }

            @Override
            public void onException(Exception e, ProcessSession session) {
                ack(pipeline);
            }
        });

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
