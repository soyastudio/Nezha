package soya.framework.pipeline.deployment;

import com.google.common.eventbus.Subscribe;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soya.framework.pipeline.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineDeployService implements ServiceEventListener<PipelineDeployEvent> {
    private static final Logger logger = LoggerFactory.getLogger(PipelineDeployService.class);

    private CompositePipelineDeployer deployer;

    private File pipelineHome;
    private File deploymentDir;

    private Map<String, PipelineDeployment> deployments = new ConcurrentHashMap<>();

    private Map<String, PipelineDeployEvent> events = new ConcurrentHashMap<>();

    public List<PipelineDeployment> getDeployments() {
        List<PipelineDeployment> list = new ArrayList<>(deployments.values());
        Collections.sort(list);
        return list;
    }

    public PipelineDeployment getDeployment(String name) {
        return deployments.get(name);
    }

    @Override
    public void initialize() {
        logger.info("Initializing PipelineDeployService...");

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

        CompositePipelineDeployer.Builder deployBuilder =  CompositePipelineDeployer.builder();
        PipelineServer.getInstance().getPipelineEngineRegistration().registers().forEachRemaining(e -> {
            deployBuilder.addPipelineDeployer(e.getValue().getDeployer());
        });
        this.deployer = deployBuilder.build();

        Timer timer = new Timer();
        File[] files = pipelineHome.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                PipelineDeployer pipelineDeployer = deployer;
                pipelineDeployer = deployer.getPipelineDeployer(file);

                PipelineDeployment deployment = new PipelineDeployment(file);
                deployments.put(deployment.getName(), deployment);

                System.out.println("===================== " + deployment.getName() + ": " + pipelineDeployer.getClass().getName());

                Optional<Pipeline> opt = deployer.deploy(deployment);
                if(opt.isPresent()) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            opt.get().init();
                        }
                    }, new Random().nextInt(5000));
                }
            }
        }

        timer.schedule(new DeploymentScanner(deploymentDir), 30000L, 15000L);
    }

    @Subscribe
    public void onEvent(PipelineDeployEvent event) {

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
                if (optional.isPresent()) {
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
