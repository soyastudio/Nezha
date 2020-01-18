package soya.framework.settler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WorkflowEngine extends Components {
    protected static WorkflowEngine instance;

    private ExecutorService executorService;

    static {
        instance = new WorkflowEngine();
        register(Processor.class.getPackage().getName());
    }

    protected WorkflowEngine() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public Future<ProcessSession> execute(Workflow workflow) {
        System.out.println("-------------------- execute workflow: " + workflow.getName());

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
