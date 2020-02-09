package soya.framework.nezha;

public interface WorkflowCallback {
    void onSuccess(ProcessSession session);

    void onException(Exception e, ProcessSession session);

}
