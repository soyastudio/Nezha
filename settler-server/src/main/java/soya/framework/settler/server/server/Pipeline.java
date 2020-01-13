package soya.framework.settler.server.server;

import java.io.File;
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

    private Properties properties;

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

    protected void init() {
        evaluateProperties(configuration.metadata);
        defineFunctions(configuration.functions);
        loadData(configuration.initFlow);
    }

    private void evaluateProperties(Properties metadata) {
        System.out.println("--------------------- todo: evaluate properties for" + name);
    }

    private void defineFunctions(Properties functions) {
        System.out.println("--------------------- todo: define functions for " + name);
    }

    private void loadData(Flow init) {
        System.out.println("--------------------- todo: load cached data for " + name);
    }

    public Future<PipelineExecution> execute() {
        return Server.getInstance().getService(ExecutorService.class).submit(new Callable<PipelineExecution>() {
            @Override
            public PipelineExecution call() throws Exception {
                return null;
            }
        });
    }

    public static Builder builder(File base) {
        return new Builder(base);
    }

    static class Builder {
        private File base;
        private Properties metadata;
        private Properties functions;
        private Flow initFlow;
        private Flow mainFlow;
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

        public Builder initFlow(Flow flow) {
            this.initFlow = flow;
            return this;
        }

        public Builder mainFlow(Flow flow) {
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

            pipeline.init();
            return pipeline;
        }
    }

    static class Configuration {

        private Properties metadata;
        private Properties functions;
        private Flow initFlow;
        private Flow mainFlow;
        private Scheduler scheduler;

        private Configuration() {
        }

        public Properties getMetadata() {
            return metadata;
        }

        public Properties getFunctions() {
            return functions;
        }

        public Flow getInitFlow() {
            return initFlow;
        }

        public Flow getMainFlow() {
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

    static class Flow {

    }
}
