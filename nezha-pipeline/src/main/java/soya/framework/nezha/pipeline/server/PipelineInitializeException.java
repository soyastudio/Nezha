package soya.framework.nezha.pipeline.server;

public class PipelineInitializeException extends PipelineCreateException {
    public PipelineInitializeException() {
    }

    public PipelineInitializeException(String message) {
        super(message);
    }

    public PipelineInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineInitializeException(Throwable cause) {
        super(cause);
    }
}
