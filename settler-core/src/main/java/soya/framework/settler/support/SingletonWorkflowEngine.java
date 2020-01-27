package soya.framework.settler.support;

import soya.framework.settler.*;

import java.util.concurrent.ExecutionException;
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

    private Future<ProcessSession> run(DataObject dataObject, Workflow workflow) {
        return executorService.submit(() -> {
            ProcessSession session = new DefaultProcessSession(workflow.getContext());
            if (dataObject != null) {
                session.update(dataObject);
            }

            for (ExecutableNode node : workflow.getTasks()) {
                TaskDefinition.create(node, workflow.getContext()).process(session);
            }
            return session;
        });
    }

    public static SingletonWorkflowEngine getInstance() {
        return instance;
    }

    @Override
    public void execute(Workflow workflow) throws ProcessException {
        run(null, workflow);
    }

    @Override
    public void execute(Workflow workflow, WorkflowCallback callback) throws ProcessException {
        execute(null, workflow, callback);
    }

    @Override
    public void execute(DataObject data, Workflow workflow, WorkflowCallback callback) throws ProcessException {

        Future<ProcessSession> future = run(data, workflow);
        if (callback != null) {
            while (!future.isDone()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    throw new ProcessException(e);
                }
            }

            try {
                callback.onSuccess(future.get());

            } catch (InterruptedException e) {
                throw new ProcessException(e);

            } catch (ExecutionException e) {
                try {
                    callback.onException(e, future.get());

                } catch (Exception ex) {
                    throw new ProcessException(e);
                }
            }
        }
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
