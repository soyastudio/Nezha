package soya.framework.settler.server;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import soya.framework.settler.server.server.*;

@SpringBootApplication(scanBasePackages = {"soya.framework.settler.server"})
public class SettlerServer extends PipelineServer {
    private static final Logger logger = LoggerFactory.getLogger(SettlerServer.class);

    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SettlerServer.class, args);
    }

    @EventListener(classes = {ApplicationReadyEvent.class})
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.applicationContext = event.getApplicationContext();
        applicationContext.getBeansOfType(ServiceEventListener.class).values().forEach(e -> {
            register(e);
        });
    }

    @Override
    public String getEnvironmentProperty(String key) {
        return applicationContext.getEnvironment().getProperty(key);
    }

    @Override
    public <T> T getService(Class<T> type) {
        return applicationContext.getBean(type);
    }

    @Bean
    PipelineDeployService pipelineDeploymentService() {
        return new PipelineDeployService();
    }

    @Bean
    PipelineLogService pipelineLogService() {
        return new PipelineLogService();
    }

    @Bean
    PipelineService pipelineService(@Autowired Scheduler scheduler) {
        return new PipelineService(scheduler);
    }
}
