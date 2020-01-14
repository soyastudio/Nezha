package soya.framework.settler.server.server;

import com.google.gson.JsonArray;

import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Pipeline {

    private final String name;
    private File base;
    private Configuration configuration;

    private ExternalContext externalContext;
    private Properties properties = new Properties();
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

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

    public void init(ExternalContext externalContext) {
        if (this.externalContext != null) {
            throw new IllegalStateException("Pipeline is already initialized.");
        }
        this.externalContext = externalContext;

        evaluateProperties(configuration.metadata, externalContext);
        defineFunctions(configuration.functions, externalContext);
        loadData(configuration.initFlow, externalContext);
        initWorkflow(configuration.mainFlow, externalContext);

    }

    private void evaluateProperties(Properties metadata, ExternalContext externalContext) {
        if(metadata != null) {
            Enumeration<?> enumeration = metadata.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = configuration.metadata.getProperty(key);

                key = key.replaceAll("\\.", "_");
                value = evaluateProperty(value, externalContext);

                System.out.println("----------- set property " + key + " = " + value);

                properties.setProperty(key, value);
            }
        }
    }

    private String evaluateProperty(String value, ExternalContext externalContext) {
        return value;
    }

    private void defineFunctions(Properties functions, ExternalContext externalContext) {
        System.out.println("--------------------- todo: define functions for " + name);
    }

    private void loadData(Properties init, ExternalContext externalContext) {
        System.out.println("--------------------- todo: load cached data for " + name);
    }

    private void initWorkflow(JsonArray mainFlow, ExternalContext externalContext) {

        System.out.println("--------------------- todo: create workflow for " + name);
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
            if(mainFlow == null) {
                throw new IllegalArgumentException("'main-flow' is required.");
            }

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
