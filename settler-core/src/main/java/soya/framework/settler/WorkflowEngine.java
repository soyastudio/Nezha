package soya.framework.settler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WorkflowEngine extends Components {
    protected static WorkflowEngine instance;

    private ProcessContext context;
    private ExecutorService executorService;

    static {
        instance = new WorkflowEngine();
        register(Processor.class.getPackage().getName());
    }

    protected WorkflowEngine() {
        this.context = new DefaultProcessContext();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    protected WorkflowEngine(ExternalContext externalContext) {
        this.context = new DefaultProcessContext(externalContext);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    protected WorkflowEngine(ProcessContext context, ExecutorService executorService) {
        this.context = context;
        this.executorService = executorService;
    }

    public Future<ProcessSession> execute(Workflow workflow) {
        return executorService.submit(() -> {
            ProcessSession session = new DefaultProcessSession(workflow.getContext());
            for (ExecutableNode node : workflow.getTasks()) {


            }

            return session;
        });
    }

    public static WorkflowEngine getInstance() {
        return instance;
    }

    static class DefaultProcessContext implements ProcessContext {

        private ExternalContext externalContext;
        private Properties configuration;

        public DefaultProcessContext() {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = WorkflowEngine.class.getClassLoader();
            }

            configuration = new Properties();
            InputStream inputStream = classLoader.getResourceAsStream("settler-config.properties");
            if (inputStream != null) {
                try {
                    configuration.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public DefaultProcessContext(ExternalContext externalContext) {
            this.externalContext = externalContext;
        }

        @Override
        public ExternalContext getExternalContext() {
            return externalContext;
        }
    }

    static class DefaultProcessSession implements ProcessSession {
        private ProcessContext context;

        private DefaultProcessSession(ProcessContext context) {
            this.context = context;
        }

        @Override
        public ProcessContext getContext() {
            return context;
        }
    }
}
