package soya.framework.pipeline;

public interface PipelineEngineRegister<T extends Pipeline> {

    PipelineDeployer<T> getDeployer();

}
