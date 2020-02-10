package soya.framework.nezha.pipeline;

public interface ServiceEventListener<E extends ServiceEvent> {
    void onEvent(E e);
}
