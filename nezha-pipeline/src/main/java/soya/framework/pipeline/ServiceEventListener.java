package soya.framework.pipeline;

public interface ServiceEventListener<E extends ServiceEvent> {
    void initialize();

    void onEvent(E e);
}
