package soya.framework.nezha.pipeline;

public class ServiceExceptionEvent extends ServiceEvent {
    private Exception exception;

    private ServiceExceptionEvent(Exception exception) {
        super();
        this.exception = exception;
    }

    public static ServiceExceptionEvent newInstance(Exception ex) {
        return new ServiceExceptionEvent(ex);
    }
}
