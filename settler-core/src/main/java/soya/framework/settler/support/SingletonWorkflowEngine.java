package soya.framework.settler.support;

import soya.framework.settler.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingletonWorkflowEngine extends Components implements WorkflowEngine {
    protected static SingletonWorkflowEngine instance;

    private ExecutorService executorService;

    static {
        instance = new SingletonWorkflowEngine();
        register(Processor.class.getPackage().getName());
    }

    protected SingletonWorkflowEngine() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public Future<ProcessSession> execute(Workflow workflow) {


        return executorService.submit(() -> {
            ProcessSession session = new DefaultProcessSession(workflow.getContext());
            for (ExecutableNode node : workflow.getTasks()) {
                TaskExecution execution = create(node, session);
                execution.execute(executorService);
            }

            return session;
        });
    }

    public static SingletonWorkflowEngine getInstance() {
        return instance;
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
