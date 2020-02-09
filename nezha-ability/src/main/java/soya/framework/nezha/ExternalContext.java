package soya.framework.nezha;

public interface ExternalContext {
    String getEnvironmentProperty(String key);

    <T> T getService(Class<T> type);
}
