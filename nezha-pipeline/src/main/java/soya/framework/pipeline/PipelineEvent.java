package soya.framework.pipeline;

public abstract class PipelineEvent extends ServiceEvent {
    private final String pipeline;
    protected long timeout = 30000L;

    public PipelineEvent(String pipeline) {
        super();
        this.pipeline = pipeline;
    }

    public String getPipeline() {
        return pipeline;
    }

    public long getTimeout() {
        return timeout;
    }
}
