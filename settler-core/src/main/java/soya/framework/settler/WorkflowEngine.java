package soya.framework.settler;

public interface WorkflowEngine {
    void execute(Workflow workflow) throws ProcessException;

    void execute(Workflow workflow, WorkflowCallback callback) throws ProcessException;
}
