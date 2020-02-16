package soya.framework.pipeline;

import java.io.File;
import java.util.Optional;

public interface PipelineDeployer<T extends Pipeline> {

    File getPipelineFile(File dir);

    Optional<T> deploy(PipelineDeployment deployment);

    void stop(PipelineDeployment deployment);
}
