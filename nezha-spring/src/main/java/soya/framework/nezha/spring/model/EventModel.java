package soya.framework.nezha.spring.model;

import soya.framework.pipeline.ServiceEventListener;

import java.util.List;

public class EventModel {
    private Class<?> eventType;
    private List<Class<? extends ServiceEventListener>> handlers;

    public EventModel(Class<?> eventType, List<Class<? extends ServiceEventListener>> handlers) {
        this.eventType = eventType;
        this.handlers = handlers;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public List<Class<? extends ServiceEventListener>> getHandlers() {
        return handlers;
    }
}
