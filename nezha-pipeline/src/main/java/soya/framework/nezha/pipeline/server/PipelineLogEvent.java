package soya.framework.nezha.pipeline.server;

public class PipelineLogEvent extends ServiceEvent {
    private final String pipeline;
    private final EventType eventType;
    private String message;

    private PipelineLogEvent(String pipeline, EventType eventType) {
        super();
        this.pipeline = pipeline;
        this.eventType = eventType;
    }

    public String getPipeline() {
        return pipeline;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public static Builder builder(String pipeline, EventType eventType) {
        return new Builder(pipeline, eventType);
    }


    static enum EventType {
        CREATE
    }

    static class Builder {
        private String pipeline;
        private EventType eventType;

        private Builder(String pipeline, EventType eventType) {
            this.pipeline = pipeline;
            this.eventType = eventType;
        }

        public PipelineLogEvent create() {
            PipelineLogEvent event = new PipelineLogEvent(pipeline, eventType);

            return event;
        }
    }
}
