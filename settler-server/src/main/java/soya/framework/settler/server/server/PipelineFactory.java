package soya.framework.settler.server.server;

import java.io.File;

public interface PipelineFactory {
    Pipeline create(File base) throws PipelineCreateException;
}
