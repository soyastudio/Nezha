package soya.framework.settler.server.server;

import java.io.File;

public class PipelineScheduleEvent extends PipelineEvent {
    private final File dir;

    public PipelineScheduleEvent(String pipeline, File dir) {
        super(pipeline);
        this.dir = dir;
    }

    public File getDir() {
        return dir;
    }
}
