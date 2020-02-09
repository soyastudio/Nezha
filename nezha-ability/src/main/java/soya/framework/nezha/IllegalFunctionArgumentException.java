package soya.framework.nezha;

public class IllegalFunctionArgumentException extends ProcessorBuildException {
    public IllegalFunctionArgumentException() {
    }

    public IllegalFunctionArgumentException(String message) {
        super(message);
    }

    public IllegalFunctionArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFunctionArgumentException(Throwable cause) {
        super(cause);
    }
}
