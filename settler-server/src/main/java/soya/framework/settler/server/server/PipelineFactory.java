package soya.framework.settler.server.server;

import java.io.File;
import java.io.IOException;

public interface PipelineFactory {
    Pipeline create(File base) throws IOException;
}
