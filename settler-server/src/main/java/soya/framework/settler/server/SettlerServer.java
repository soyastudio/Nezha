package soya.framework.settler.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import soya.framework.settler.server.server.Server;
import soya.framework.settler.server.server.ServiceEventListener;

@SpringBootApplication(scanBasePackages = {"soya.framework.settler.server"})
public class SettlerServer extends Server {
    private static final Logger logger = LoggerFactory.getLogger(SettlerServer.class);

    public static void main(String[] args) {
        SpringApplication.run(SettlerServer.class, args);
    }

    @EventListener(classes = {ApplicationReadyEvent.class})
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        applicationContext.getBeansOfType(ServiceEventListener.class).values().forEach(e -> {
            register(e);
        });
    }
}
