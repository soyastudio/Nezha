package soya.framework.settler.server.server;

public interface ExternalContext {
    String getEnvironmentProperty(String key);

    <T> T getService(Class<T> type);
}
