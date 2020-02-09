package soya.framework.nezha.component;

import soya.framework.nezha.ProcessException;

public class TransformException extends ProcessException {
    public TransformException() {
    }

    public TransformException(String message) {
        super(message);
    }

    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformException(Throwable cause) {
        super(cause);
    }
}
