package soya.framework.pipeline;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceExceptionHandler implements ServiceEventListener<ServiceExceptionEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @Override
    public void initialize() {
        logger.info("Initializing ServiceExceptionHandler...");
    }

    @Subscribe
    public void onEvent(ServiceExceptionEvent serviceExceptionEvent) {

    }
}
