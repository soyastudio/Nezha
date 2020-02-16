package soya.framework.pipeline;

import com.google.common.eventbus.Subscribe;

public class EventStoreService implements ServiceEventListener<TraceableEvent> {

    @Override
    public void initialize() {

    }

    @Subscribe
    public void onEvent(TraceableEvent traceableEvent) {

    }
}
