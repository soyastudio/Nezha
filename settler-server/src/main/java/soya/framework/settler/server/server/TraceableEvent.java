package soya.framework.settler.server.server;

public interface TraceableEvent extends AutoCloseable {
    String getId();

    long getTimeout();

    boolean isClosed();
}
