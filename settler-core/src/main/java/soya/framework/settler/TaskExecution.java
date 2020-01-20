package soya.framework.settler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskExecution implements Callable<ProcessSession> {
    private ExecutableNode executableNode;
    private ProcessSession session;

    public TaskExecution(ExecutableNode executableNode, ProcessSession session) {
        this.executableNode = executableNode;
        this.session = session;
    }

    public static Processor create(FunctionNode function, ProcessSession session) {
        return Components.getProcessBuilder(function.getName()).build(function.getArguments(), session);
    }

    @Override
    public ProcessSession call() throws Exception {
        TaskDefinition task = TaskDefinition.create(executableNode);
        // TODO:

        System.out.println("--------------------- !!!");
        return session;
    }

    public Future<ProcessSession> execute(ExecutorService executorService) {
        return executorService.submit(this);
    }
}
