package soya.framework.nezha.pipeline.server;

import com.google.common.eventbus.Subscribe;
import org.apache.commons.io.input.ReversedLinesFileReader;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
                initPipelineLog(pipelineLogEvent.getPipeline());
                break;
            default:
                ;
        }
    }

    public String read(String pipeline, int offset,
                       int limit, boolean reverse, boolean withIndex) throws IOException {

        StringBuilder builder = new StringBuilder();
        File file = new File(baseDir, pipeline + ".log");
        ReversedLinesFileReader reversedLinesFileReader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8);

        String line = reversedLinesFileReader.readLine();
        int count = 0;
        while (line != null) {
            count++;
            if (count > offset + limit) {
                break;
            } else if (count > offset) {
                if (withIndex) {
                    builder.append(count - 1).append("\t");
                }
                builder.append(line).append("\n");
            }

            line = reversedLinesFileReader.readLine();
        }
        reversedLinesFileReader.close();
        return builder.toString();

    }

    private void initPipelineLog(String pipeline) {
        File file = new File(baseDir, pipeline + ".log");
        if (!file.exists()) {
            ((Runnable) () -> {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    PipelineServer.getInstance().publish(ServiceExceptionEvent.newInstance(e));
                }
            }).run();
        }
    }
}
