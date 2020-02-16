package soya.framework.pipeline;

import java.util.Set;

public interface EventService {
    Set<Class<? extends ServiceEvent>> eventTypes();

    Set<Class<? extends ServiceEventListener>> listeners(Class<? extends ServiceEvent> eventType);

    void publish(ServiceEvent event);
}
