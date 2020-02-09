package soya.framework.nezha;

public interface WorkflowEngine {
    void execute(Workflow workflow) throws ProcessException;

    void execute(Workflow workflow, WorkflowCallback callback) throws ProcessException;

    void execute(DataObject data, Workflow workflow, WorkflowCallback callback) throws ProcessException;
}
