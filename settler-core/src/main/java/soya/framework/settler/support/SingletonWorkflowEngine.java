package soya.framework.settler.support;

import soya.framework.settler.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingletonWorkflowEngine implements WorkflowEngine {
    protected static SingletonWorkflowEngine instance;

    private ExecutorService executorService;

    static {
        instance = new SingletonWorkflowEngine();
    }

    protected SingletonWorkflowEngine() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public Future<ProcessSession> execute(Workflow workflow) {
        return executorService.submit(() -> {
            ProcessSession session = new DefaultProcessSession(workflow.getContext());
            for (ExecutableNode node : workflow.getTasks()) {
                TaskDefinition.create(node, workflow.getContext()).process(session);
            }
            return session;
        });
    }

    public static SingletonWorkflowEngine getInstance() {
        return instance;
    }

    static class DefaultProcessSession implements ProcessSession {
        private ProcessContext context;
        private DataObject dataObject;

        private DefaultProcessSession(ProcessContext context) {
            this.context = context;
        }

        @Override
        public ProcessContext getContext() {
            return context;
        }

        @Override
        public DataObject current() {
            return dataObject;
        }

        @Override
        public synchronized void update(DataObject dataObject) {
            this.dataObject = dataObject;
        }
    }
}
