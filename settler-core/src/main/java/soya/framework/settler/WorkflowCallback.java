package soya.framework.settler;

public interface WorkflowCallback {
    void onSuccess(ProcessSession session);

    void onException(Exception e, ProcessSession session);

}
