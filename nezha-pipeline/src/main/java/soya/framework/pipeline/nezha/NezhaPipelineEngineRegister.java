package soya.framework.pipeline.nezha;

import soya.framework.pipeline.PipelineDeployer;
import soya.framework.pipeline.PipelineEngineRegister;

public class NezhaPipelineEngineRegister implements PipelineEngineRegister<NezhaPipeline> {

    private PipelineDeployer deployer = new NezhaPipelineDeployer();

    @Override
    public PipelineDeployer<NezhaPipeline> getDeployer() {
        return deployer;
    }
}
