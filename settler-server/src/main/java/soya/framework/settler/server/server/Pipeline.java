package soya.framework.settler.server.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pipeline {

    private final String name;
    private File base;
    private Configuration configuration;

    private ExternalContext externalContext;
    private Properties properties = new Properties();
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    private List<TaskDefinition> taskDefinitions = new ArrayList<>();

    private Pipeline(File base) {
        this.base = base;
        this.name = base.getName();
    }

    public File getBaseDir() {
        return base;
    }

    public String getName() {
        return name;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void init(ExternalContext externalContext) throws PipelineInitializeException {
        if (this.externalContext != null) {
            throw new PipelineInitializeException("Pipeline is already initialized.");
        }
        this.externalContext = externalContext;

        evaluateProperties(configuration.metadata, externalContext);
        defineFunctions(configuration.functions, externalContext);
        loadData(configuration.initFlow, externalContext);
        initWorkflow(configuration.mainFlow, externalContext);

    }

    private void evaluateProperties(Properties metadata, ExternalContext externalContext) throws PipelineInitializeException {
        if (metadata != null) {
            Enumeration<?> enumeration = metadata.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = configuration.metadata.getProperty(key);

                key = key.replaceAll("\\.", "_");
                value = evaluateProperty(value, externalContext);

                properties.setProperty(key, value);
            }
        }
    }

    private String evaluateProperty(String value, ExternalContext externalContext) throws PipelineInitializeException {
        final StringBuffer sb = new StringBuffer();
        final Pattern pattern =
                Pattern.compile("\\$\\{(.*?)\\}", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            final String key = matcher.group(1);
            final String replacement = externalContext.getEnvironmentProperty(key);
            if (replacement == null) {
                throw new IllegalArgumentException(
                        "Template contains unmapped key: "
                                + key);
            }
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private void defineFunctions(Properties functions, ExternalContext externalContext) throws PipelineInitializeException {
        System.out.println("--------------------- todo: define functions for " + name);
    }

    private void loadData(Properties init, ExternalContext externalContext) throws PipelineInitializeException {
        System.out.println("--------------------- todo: load cached data for " + name);
    }

    private void initWorkflow(JsonArray mainFlow, ExternalContext externalContext) throws PipelineInitializeException {
        System.out.println("--------------------- todo: create workflow for " + name);
        if (mainFlow == null || mainFlow.size() == 0) {
            throw new PipelineInitializeException("main-flow is not set!");
        }

        mainFlow.forEach(e -> {
            taskDefinitions.add(TaskDefinition.parse(e));
        });


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(taskDefinitions));
    }

    public static Builder builder(File base) {
        return new Builder(base);
    }

    static class Builder {
        private File base;
        private Properties metadata;
        private Properties functions;
        private Properties initFlow;
        private JsonArray mainFlow;
        private Scheduler scheduler;

        private Builder(File base) {
            this.base = base;
        }

        public Builder metadata(Properties properties) {
            this.metadata = properties;
            return this;
        }

        public Builder functions(Properties properties) {
            this.functions = properties;
            return this;
        }

        public Builder initFlow(Properties flow) {
            this.initFlow = flow;
            return this;
        }

        public Builder mainFlow(JsonArray flow) {
            this.mainFlow = flow;
            return this;
        }

        public Builder scheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;

        }

        public Pipeline create() {
            Pipeline pipeline = new Pipeline(base);
            Configuration configuration = new Configuration();
            configuration.metadata = metadata;
            configuration.functions = functions;
            configuration.initFlow = initFlow;
            configuration.mainFlow = mainFlow;
            configuration.scheduler = scheduler;
            pipeline.configuration = configuration;

            return pipeline;
        }
    }

    static class Configuration {

        private Properties metadata;
        private Properties functions;
        private Properties initFlow;
        private JsonArray mainFlow;
        private Scheduler scheduler;

        private Configuration() {
        }

        public Properties getMetadata() {
            return metadata;
        }

        public Properties getFunctions() {
            return functions;
        }

        public Properties getInitFlow() {
            return initFlow;
        }

        public JsonArray getMainFlow() {
            return mainFlow;
        }

        public Scheduler getScheduler() {
            return scheduler;
        }

    }

    static class Scheduler {
        private int delay;
        private int interval;
        private String calendar;

        private Scheduler() {
        }

        public int getDelay() {
            return delay;
        }

        public int getInterval() {
            return interval;
        }

        public String getCalendar() {
            return calendar;
        }
    }
}
