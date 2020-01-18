package soya.framework.settler.server.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.yaml.snakeyaml.Yaml;
import soya.framework.settler.ExecutableNode;
import soya.framework.settler.ExternalContext;
import soya.framework.settler.ProcessContext;
import soya.framework.settler.Workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pipeline implements Workflow {

    private final String name;
    private File base;
    private Configuration configuration;

    private ProcessContext context;
    private List<ExecutableNode> tasks = new ArrayList<>();

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

    @Override
    public ProcessContext getContext() {
        return context;
    }

    @Override
    public List<ExecutableNode> getTasks() {
        return tasks;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void init(ExternalContext externalContext) throws PipelineInitializeException {
        if (this.context != null) {
            throw new PipelineInitializeException("Pipeline is already initialized.");
        }
        this.context = new PipelineContext(configuration, externalContext);

        initWorkflow(configuration.mainFlow, externalContext);

    }

    private static String evaluateProperty(String value, ExternalContext externalContext) throws PipelineInitializeException {
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

    private void initWorkflow(JsonElement mainFlow, ExternalContext externalContext) throws PipelineInitializeException {
        if (mainFlow == null || mainFlow.isJsonNull()) {
            throw new PipelineInitializeException("main-flow is not set!");
        }

        if (mainFlow.isJsonArray()) {
            mainFlow.getAsJsonArray().forEach(e -> {
                tasks.add(TaskDefinition.parse(e));
            });

        } else if (mainFlow.isJsonObject()) {
            mainFlow.getAsJsonObject().entrySet().forEach(e -> {
                String key = e.getKey();
                JsonElement value = e.getValue();
                if (value.isJsonPrimitive()) {
                    tasks.add(TaskDefinition.parse(key, value));
                } else {

                }
            });

        } else {

        }
    }

    public static Pipeline fromYaml(File base, InputStream inputStream) {
        Map<String, Object> configuration = new Yaml().load(inputStream);

        Builder builder = Pipeline.builder(base);
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
                    if (e.getValue() instanceof Map) {
                        builder.initFlow(parse(e.getValue(), Properties.class, base));
                    } else {
                        throw new IllegalArgumentException("Illegal format for 'init-flow' configuration, 'map' style is expected.");
                    }
                    break;
                case "main-flow":
                    builder.mainFlow(parse(e.getValue(), JsonElement.class, base));
                    break;
                case "scheduler":
                    builder.scheduler(parse(e.getValue(), Pipeline.Scheduler.class, base));
                    break;
                default:
                    ;
            }

        });

        return builder.create();
    }

    private static <T> T parse(Object config, Class<T> type, File baseDir) {
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

        } else if (JsonArray.class.isAssignableFrom(type)) {
            JsonArray array = new JsonArray();
            if (config instanceof String) {


            } else if (config instanceof List) {
                List<Object> list = (List<Object>) config;
                list.forEach(e -> {
                    if (e instanceof String) {
                        array.add((String) e);
                    }
                });
            }

            return (T) array;

        } else {
            t = gson.fromJson(gson.toJson(config), type);
        }

        return t;
    }

    public static Builder builder(File base) {
        return new Builder(base);
    }

    static class Builder {
        private File base;
        private Properties metadata;
        private Properties functions;
        private Properties initFlow;
        private JsonElement mainFlow;
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

        public Builder mainFlow(JsonElement flow) {
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
        private JsonElement mainFlow;
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

        public JsonElement getMainFlow() {
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

    static class PipelineContext implements ProcessContext {
        private ExternalContext externalContext;

        private Properties properties = new Properties();
        private Map<String, Object> attributes = new ConcurrentHashMap<>();

        private PipelineContext(Configuration configuration, ExternalContext externalContext) throws PipelineInitializeException {
            this.externalContext = externalContext;

            evaluateProperties(configuration, externalContext);
            defineFunctions(configuration.functions, externalContext);
            loadData(configuration.initFlow, externalContext);


        }

        private void evaluateProperties(Configuration configuration, ExternalContext externalContext) throws PipelineInitializeException {
            if (configuration.metadata != null) {
                Enumeration<?> enumeration = configuration.metadata.propertyNames();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    String value = configuration.metadata.getProperty(key);

                    key = key.replaceAll("\\.", "_");
                    value = evaluateProperty(value, externalContext);

                    properties.setProperty(key, value);
                }
            }
        }

        private void defineFunctions(Properties functions, ExternalContext externalContext) throws PipelineInitializeException {
        }

        private void loadData(Properties init, ExternalContext externalContext) throws PipelineInitializeException {
        }

        @Override
        public ExternalContext getExternalContext() {
            return externalContext;
        }
    }
}
