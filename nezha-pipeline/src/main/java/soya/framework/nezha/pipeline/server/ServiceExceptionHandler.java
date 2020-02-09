package soya.framework.nezha.pipeline.server;

import com.google.common.eventbus.Subscribe;

public class ServiceExceptionHandler implements ServiceEventListener<ServiceExceptionEvent> {

    @Subscribe
    public void onEvent(ServiceExceptionEvent serviceExceptionEvent) {

    }
}
