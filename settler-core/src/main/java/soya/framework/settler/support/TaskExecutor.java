package soya.framework.settler.support;

import soya.framework.settler.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskExecutor implements Callable<ProcessSession> {
    private ExecutableNode executableNode;
    private ProcessSession session;

    public TaskExecutor(ExecutableNode executableNode, ProcessSession session) {
        this.executableNode = executableNode;
        this.session = session;
    }

    @Override
    public ProcessSession call() throws Exception {
        TaskDefinition task = TaskDefinition.create(executableNode, session);
        // TODO:

        System.out.println("--------------------- !!!");
        return session;
    }

    public Future<ProcessSession> execute(ExecutorService executorService) {
        return executorService.submit(this);
    }
}
