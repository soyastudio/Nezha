package soya.framework.nezha;

public class ProcessorBuildException extends EvaluateException {
    public ProcessorBuildException() {
    }

    public ProcessorBuildException(String message) {
        super(message);
    }

    public ProcessorBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessorBuildException(Throwable cause) {
        super(cause);
    }
}
