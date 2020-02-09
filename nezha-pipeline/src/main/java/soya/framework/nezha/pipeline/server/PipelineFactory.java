package soya.framework.nezha.pipeline.server;

import java.io.File;

public interface PipelineFactory {
    Pipeline create(File base) throws PipelineCreateException;
}
