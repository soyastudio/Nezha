package soya.framework.pipeline.nezha;

import soya.framework.pipeline.PipelineDeployer;
import soya.framework.pipeline.PipelineDeployment;

import java.io.File;
import java.util.Optional;

public class NezhaPipelineDeployer implements PipelineDeployer<NezhaPipeline> {
    private static final String[] paths = {"/pipeline.yaml", "/pipeline.json", "pipeline.xml"};

    @Override
    public Optional<String> getPipelineFile(File dir) {
        for (String path : paths) {
            File file = new File(dir, path);
            if (file.exists() && file.isFile()) {
                return Optional.of(path);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<NezhaPipeline> deploy(PipelineDeployment deployment) {
        return Optional.empty();
    }

}
