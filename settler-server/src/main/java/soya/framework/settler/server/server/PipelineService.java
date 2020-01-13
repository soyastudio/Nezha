package soya.framework.settler.server.server;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

        ack(pipeline);
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
        Server.getInstance().publish(new PipelineAckEvent(pipeline.getName()));
    }

    static class PipelineJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            String pipeline = jobExecutionContext.getJobDetail().getKey().getName();
            Server.getInstance().publish(new PipelineTriggerEvent(pipeline));
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
                    Server.getInstance().publish(new PipelineAckEvent(event.getPipeline()));
                }
            }
        }
    }

    static class DefaultPipelineFactory implements PipelineFactory {

        @Override
        public Pipeline create(File base) throws IOException {
            Pipeline.Builder builder = Pipeline.builder(base);

            File pipelineYaml = new File(base, "pipeline.yaml");
            Map<String, Object> configuration = new Yaml().load(new FileInputStream(pipelineYaml));
            configuration.entrySet().forEach(e -> {
                String key = e.getKey();
                switch (key) {
                    case "metadata":
                        builder.metadata(parse(e.getValue(), Properties.class, base));
                        break;
                    case "functions":
                        builder.functions(parse(e.getValue(), Properties.class, base));
                        break;
                    case "init-flow":
                        builder.initFlow(parse(e.getValue(), Pipeline.Flow.class, base));
                        break;
                    case "main-flow":
                        builder.mainFlow(parse(e.getValue(), Pipeline.Flow.class, base));
                        break;
                    case "scheduler":
                        builder.scheduler(parse(e.getValue(), Pipeline.Scheduler.class, base));
                        break;
                    default:
                        ;
                }

            });
            Pipeline pipeline = builder.create();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(pipeline.getConfiguration()));

            return pipeline;
        }

        private <T> T parse(Object config, Class<T> type, File baseDir) {
            T t = null;
            Gson gson = new Gson();
            if (Properties.class.isAssignableFrom(type)) {
                Properties properties = new Properties();
                if (config instanceof Map) {
                    Map<String, String> map = (Map<String, String>) config;
                    map.entrySet().forEach(e -> {
                        properties.setProperty(e.getKey(), e.getValue());
                    });

                } else {
                    File file = new File(baseDir, config.toString());
                    try {
                        properties.load(new FileInputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

               t = (T) properties;

            } else {
                t = gson.fromJson(gson.toJson(config), type);
            }

            return t;
        }
    }
}
