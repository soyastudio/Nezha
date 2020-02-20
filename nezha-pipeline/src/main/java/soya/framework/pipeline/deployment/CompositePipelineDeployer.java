package soya.framework.pipeline.deployment;

import com.google.common.collect.ImmutableSet;
import soya.framework.pipeline.Pipeline;
import soya.framework.pipeline.PipelineDeployer;
import soya.framework.pipeline.PipelineDeployment;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class CompositePipelineDeployer {

    private static PipelineDeployer defaultDeployer = new EmptyPipelineDeployer();
    private ImmutableSet<PipelineDeployer> deployers;

    private CompositePipelineDeployer(Set<PipelineDeployer> deployers) {
        this.deployers = ImmutableSet.copyOf(deployers);
    }

    public PipelineDeployer getPipelineDeployer(File dir) {
        for (PipelineDeployer deployer : deployers) {
            if (deployer.getPipelineFile(dir).isPresent()) {
                return deployer;
            }
        }

        return new EmptyPipelineDeployer();
    }

    public static PipelineDeployer defaultDeployer() {
        return defaultDeployer;
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
        public Optional<String> getPipelineFile(File dir) {
            return Optional.empty();
        }

        @Override
        public Optional<Pipeline> deploy(PipelineDeployment deployment) {
            return Optional.empty();
        }
    }
}
