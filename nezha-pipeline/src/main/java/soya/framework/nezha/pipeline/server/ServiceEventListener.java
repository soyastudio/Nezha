package soya.framework.nezha.pipeline.server;

public interface ServiceEventListener<E extends ServiceEvent> {
    void onEvent(E e);
}
