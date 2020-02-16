package soya.framework.pipeline;

import com.google.common.eventbus.Subscribe;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soya.framework.nezha.ProcessSession;
import soya.framework.nezha.WorkflowCallback;
import soya.framework.nezha.support.SingletonWorkflowEngine;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineService implements ServiceEventListener<PipelineEvent> {
    static final Logger logger = LoggerFactory.getLogger(PipelineService.class);

    private Map<String, PipelineEngineRegister> engines = new HashMap<>();

    private Map<String, Pipeline> pipelines = new ConcurrentHashMap<>();
    private Map<String, PipelineEvent> currentEvents = new ConcurrentHashMap<>();


    @Override
    public void initialize() {
        logger.info("Initializing PipelineService...");

        PipelineServer.getInstance().getPipelineEngineRegistration().registers().forEachRemaining(e -> {
            engines.put(e.getKey(), e.getValue());
        });


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

        logger.warn("TODO");

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
}
