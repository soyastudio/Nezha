package soya.framework.settler.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import soya.framework.settler.server.server.AbstractServer;

@SpringBootApplication(scanBasePackages = {"soya.framework.settler.server"})
public class SettlerServer extends AbstractServer {

    public static void main(String[] args) {
        SpringApplication.run(SettlerServer.class, args);
    }
}
