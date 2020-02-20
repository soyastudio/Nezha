package soya.framework.pipeline;

import org.apache.commons.io.FileUtils;
import soya.framework.pipeline.deployment.DeploymentException;
import soya.framework.pipeline.deployment.DeploymentTimeoutException;

import java.io.File;
import java.io.IOException;

public class PipelineDeployment implements Comparable<PipelineDeployment> {

    private transient final File baseDir;
    private transient PipelineDeployer<?> deployer;

    private String name;
    private String configureFile;
    private DeploymentState state;

    public PipelineDeployment(File baseDir, PipelineDeployer<?> deployer) {
        this.baseDir = baseDir;
        this.deployer = deployer;

        this.name = baseDir.getName();
        this.configureFile = deployer.getPipelineFile(baseDir).get();

        this.state = DeploymentState.CREATED;

    }

    public File getBaseDir() {
        return baseDir;
    }

    public String getName() {
        return name;
    }

    public String getConfigureFile() {
        return configureFile;
    }

    public DeploymentState getState() {
        return state;
    }

    public void delete() {
        try {
            FileUtils.forceDelete(getBaseDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected File copyDirectory(File src, File dest) throws IOException {
        File dir = new File(dest, src.getName());
        if (!dir.exists()) {
            dir.mkdir();
        }
        FileUtils.copyDirectory(src, dir);

        return dir;
    }

    private void waitForDeploymentReady(long timeout) throws DeploymentTimeoutException {
        long timestamp = System.currentTimeMillis();
        while (processing()) {
            if (System.currentTimeMillis() - timestamp > timeout) {
                throw new DeploymentTimeoutException();
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new DeploymentException(e);
            }
        }

    }

    private boolean processing() {
        return DeploymentState.STARTING.equals(state) || DeploymentState.STOPPING.equals(state);
    }

    @Override
    public int compareTo(PipelineDeployment o) {
        return this.name.compareTo(o.name);
    }

    static class DeploymentDetails {

    }

    static enum DeploymentState {
        CREATED, STARTING, STARTED, STOPPING, STOPPED, FAILED
    }
}
