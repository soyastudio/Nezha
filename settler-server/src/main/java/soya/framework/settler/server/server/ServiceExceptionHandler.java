package soya.framework.settler.server.server;

import com.google.common.eventbus.Subscribe;

public class ServiceExceptionHandler implements ServiceEventListener<ServiceExceptionEvent> {

    @Subscribe
    public void onEvent(ServiceExceptionEvent serviceExceptionEvent) {

    }
}
