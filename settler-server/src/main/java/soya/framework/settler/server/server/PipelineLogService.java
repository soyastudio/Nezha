package soya.framework.settler.server.server;

import com.google.common.eventbus.Subscribe;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

public class PipelineLogService implements ServiceEventListener<PipelineLogEvent> {

    private File baseDir;

    @PostConstruct
    public void init() {
        this.baseDir = new File(System.getProperty("pipeline.server.log.dir") + "/pipeline");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    @Subscribe
    public void onEvent(PipelineLogEvent pipelineLogEvent) {
        switch (pipelineLogEvent.getEventType()) {
            case CREATE:
                try {
                    initPipelineLog(pipelineLogEvent.getPipeline());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                ;
        }
    }

    private void initPipelineLog(String pipeline) throws IOException {
        File file = new File(baseDir, pipeline + ".log");
        if (!file.exists()) {
            System.out.println("================ creating logfile: " + file);
            file.createNewFile();
        }
    }
}
