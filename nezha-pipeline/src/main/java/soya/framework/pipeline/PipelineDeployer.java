package soya.framework.pipeline;

import java.io.File;
import java.util.Optional;

public interface PipelineDeployer<T extends Pipeline> {

    Optional<String> getPipelineFile(File dir);

    Optional<T> deploy(PipelineDeployment deployment);
}
