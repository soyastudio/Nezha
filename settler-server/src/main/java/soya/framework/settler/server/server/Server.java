package soya.framework.settler.server.server;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Server {
    private static Server instance;

    protected File home = null;
    protected File configFile = null;

    protected Server() {
        try {
            init();
            instance = this;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public File getHome() {
        return home;
    }

    protected void init() throws URISyntaxException, IOException {
        URL url = Server.class.getProtectionDomain().getCodeSource().getLocation();

        if ("jar".equalsIgnoreCase(url.getProtocol())) {
            String path = url.getPath();
            int index = path.indexOf(".jar");
            path = path.substring(0, index) + ".jar";
            path = new URI(path).getPath();
            home = new File(path);
            if (home.exists()) {
                home = home.getParentFile().getParentFile();
            }

        } else {
            File userHome = new File(System.getProperty("user.home"));
            home = new File(userHome, "Application/settler");
            if (!home.exists()) {
                home.mkdirs();
            }
        }

        System.setProperty("pipeline.server.home", home.getAbsolutePath());
        File conf = new File(home, "conf");
        if (!conf.exists()) {
            conf.mkdirs();
        }
        System.setProperty("pipeline.server.conf.dir", conf.getAbsolutePath());

        File pipeline = new File(home, "pipeline");
        if (!pipeline.exists()) {
            pipeline.mkdirs();
        }
        System.setProperty("pipeline.server.deployment.dir", pipeline.getAbsolutePath());

        configFile = new File(conf, "server-config.properties");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }

    }
}
