package soya.framework.pipeline.deployment;

import com.google.common.collect.ImmutableSet;
import soya.framework.pipeline.Pipeline;
import soya.framework.pipeline.PipelineDeployer;
import soya.framework.pipeline.PipelineDeployment;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class CompositePipelineDeployer implements PipelineDeployer {

    private ImmutableSet<PipelineDeployer> deployers;

    private CompositePipelineDeployer(Set<PipelineDeployer> deployers) {
        this.deployers = ImmutableSet.copyOf(deployers);
    }

    @Override
    public File getPipelineFile(File dir) {
        return null;
    }

    @Override
    public Optional<Pipeline> deploy(PipelineDeployment deployment) {
        return Optional.empty();
    }

    @Override
    public void stop(PipelineDeployment deployment) {

    }

    public PipelineDeployer getPipelineDeployer(File dir) {
        for (PipelineDeployer deployer : deployers) {
            if (deployer.getPipelineFile(dir) != null) {
                return deployer;
            }
        }

        return new EmptyPipelineDeployer();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Set<PipelineDeployer> set = new HashSet<>();

        private Builder() {

        }

        public Builder addPipelineDeployer(PipelineDeployer deployer) {
            set.add(deployer);
            return this;
        }

        public CompositePipelineDeployer build() {
            return new CompositePipelineDeployer(set);
        }
    }

    public static class EmptyPipelineDeployer implements PipelineDeployer {

        @Override
        public File getPipelineFile(File dir) {
            return null;
        }

        @Override
        public Optional<Pipeline> deploy(PipelineDeployment deployment) {
            return Optional.empty();
        }


        @Override
        public void stop(PipelineDeployment deployment) {

        }
    }
}
