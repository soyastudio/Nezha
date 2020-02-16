package soya.framework.pipeline.nezha;

import soya.framework.pipeline.PipelineDeployer;
import soya.framework.pipeline.PipelineDeployment;

import java.io.File;
import java.util.Optional;

public class NezhaPipelineDeployer implements PipelineDeployer<NezhaPipeline> {
    private static final String[] paths = {"/pipeline.yaml", "/pipeline.json", "pipeline.xml"};

    @Override
    public File getPipelineFile(File dir) {
        for(String path : paths) {
            File file = new File(dir, path);
            if(file.exists() && file.isFile()) {
                return file;
            }
        }

        return null;
    }

    @Override
    public Optional<NezhaPipeline> deploy(PipelineDeployment deployment) {
        return Optional.empty();
    }

    @Override
    public void stop(PipelineDeployment deployment) {

    }
}
