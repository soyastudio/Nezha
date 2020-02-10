package soya.framework.nezha.pipeline;

import java.io.File;

public interface PipelineFactory {
    Pipeline create(File base) throws PipelineCreateException;
}
