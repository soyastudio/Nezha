package soya.framework.nezha.component;

import soya.framework.nezha.ProcessException;

public class ExtractException extends ProcessException {
    public ExtractException() {
    }

    public ExtractException(String message) {
        super(message);
    }

    public ExtractException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtractException(Throwable cause) {
        super(cause);
    }
}
