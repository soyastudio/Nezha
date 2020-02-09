package soya.framework.nezha.component;

import soya.framework.nezha.ProcessException;

public class RenderException extends ProcessException {
    public RenderException() {
    }

    public RenderException(String message) {
        super(message);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenderException(Throwable cause) {
        super(cause);
    }
}
