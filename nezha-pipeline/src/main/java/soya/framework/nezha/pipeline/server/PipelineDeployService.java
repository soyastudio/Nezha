package soya.framework.nezha.pipeline.server;

import com.google.common.eventbus.Subscribe;
import org.apache.commons.io.FileUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineDeployService implements ServiceEventListener<PipelineDeployEvent> {
    private File pipelineHome;
    private File deploymentDir;
    private Map<String, PipelineDeployment> deployments = new ConcurrentHashMap<>();

    private PipelineDeployer deployer = new DefaultPipelineDeployer();

    private Map<String, PipelineDeployEvent> events = new ConcurrentHashMap<>();

    public List<PipelineDeployment> getDeployments() {
        List<PipelineDeployment> list = new ArrayList<>(deployments.values());
        Collections.sort(list);
        return list;
    }

    public PipelineDeployment getDeployment(String name) {
        return deployments.get(name);
    }

    @PostConstruct
    void init() {
        File home = PipelineServer.getInstance().getHome();
        pipelineHome = new File(home, "pipeline");
        if (!pipelineHome.exists()) {
            pipelineHome.mkdirs();
        }
        System.setProperty("pipeline.server.pipeline.dir", pipelineHome.getAbsolutePath());

        deploymentDir = new File(home, "deploy");
        if (!deploymentDir.exists()) {
            deploymentDir.mkdirs();
        }

        File[] files = pipelineHome.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                PipelineDeployment deployment = deployer.create(file);
                if (deployment != null) {
                    deployments.put(deployment.getName(), deployment);
                }
            }
        }

        Timer timer = new Timer();
        deployments.values().forEach(e -> {
            timer.schedule(new DeploymentInitializer(e, deployer), new Random().nextInt(5000));

        });

        while (!readyForScan()) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        timer.schedule(new DeploymentScanner(deploymentDir), 100L, 15000L);
    }

    private boolean readyForScan() {
        for (PipelineDeployment deployment : deployments.values()) {
            if (deployment.processing()) {
                return false;
            }
        }
        return true;
    }

    @Subscribe
    public void onEvent(PipelineDeployEvent event) {

    }

    static class DeploymentInitializer extends TimerTask {
        private PipelineDeployment deployment;
        private PipelineDeployer deployer;

        private DeploymentInitializer(PipelineDeployment deployment, PipelineDeployer deployer) {
            this.deployment = deployment;
            this.deployer = deployer;
        }

        @Override
        public void run() {
            PipelineServer.getInstance().publish(PipelineLogEvent.builder(deployment.getName(), PipelineLogEvent.EventType.CREATE).create());
            deployer.start(deployment);
        }
    }

    static class DeploymentScanner extends TimerTask {
        private File base;

        DeploymentScanner(File base) {
            this.base = base;
        }

        @Override
        public void run() {
            File[] files = base.listFiles();
            for (File file : files) {
                Optional<PipelineDeployEvent> optional = PipelineDeployEvent.fromFile(file);
                if(optional.isPresent()) {
                    PipelineServer.getInstance().publish(optional.get());
                }

                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
