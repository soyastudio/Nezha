package soya.framework.settler.server.server;

public interface ServiceEventListener<E extends ServiceEvent> {
    void onEvent(E e);
}
