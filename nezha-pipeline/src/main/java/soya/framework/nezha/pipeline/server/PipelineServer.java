package soya.framework.nezha.pipeline.server;

import com.google.common.eventbus.EventBus;
import soya.framework.nezha.ExternalContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public abstract class PipelineServer implements ExternalContext {

    private static PipelineServer instance;

    private String serverName;
    private File home;

    private EventBus eventBus;

    protected PipelineServer() {
        try {
            init();
            instance = this;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static PipelineServer getInstance() {
        return instance;
    }

    protected void init() throws URISyntaxException, IOException {
        this.serverName = name();
        this.eventBus = new EventBus(serverName);

        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
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
            home = new File(userHome, "Application/" + serverName);
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

        File configFile = new File(conf, "server-config.properties");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }

        System.setProperty("pipeline.server.home", home.getAbsolutePath());
        File log = new File(home, "log");
        if (!log.exists()) {
            log.mkdirs();
        }
        System.setProperty("pipeline.server.log.dir", log.getAbsolutePath());

        Timer timer = new Timer("Event Tracker");
        timer.schedule(new TracingTask(), 1000, 1000);

    }

    protected String name() {
        return "pipeline-server";
    }

    public String getServerName() {
        return serverName;
    }

    public File getHome() {
        return home;
    }

    public void publish(ServiceEvent event) {
        eventBus.post(event);
        if(event instanceof TraceableEvent) {

        }
    }

    protected void register(ServiceEventListener... listeners) {
        for(ServiceEventListener listener: listeners) {
            eventBus.register(listener);
        }
    }

    private class TracingTask extends TimerTask {
        @Override
        public void run() {

        }
    }
}