package soya.framework.pipeline;

public class TraceableEvent extends ServiceEvent {
    private ServiceEvent wrappedEvent;

    public TraceableEvent(ServiceEvent wrappedEvent) {
        super();
    }
}
