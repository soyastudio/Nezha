package soya.framework.pipeline;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineRepository {
    private static PipelineRepository me;

    private Map<String, Pipeline> pipelines = new ConcurrentHashMap<>();

    static {
        me = new PipelineRepository();
    }

    private PipelineRepository() {

    }

    public static PipelineRepository getInstance() {
        return me;
    }

    public void register(Pipeline pipeline) {
        pipelines.put(pipeline.getName(), pipeline);
    }
}
