package soya.framework.settler.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import soya.framework.settler.server.server.Server;

@SpringBootApplication(scanBasePackages = {"soya.framework.settler.server"})
public class SettlerServer extends Server {
    private static final Logger logger = LoggerFactory.getLogger(SettlerServer.class);

    public static void main(String[] args) {
        SpringApplication.run(SettlerServer.class, args);
    }
}
