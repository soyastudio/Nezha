package soya.framework.nezha.pipeline;

public class DeploymentTimeoutException extends RuntimeException {
    public DeploymentTimeoutException() {
    }

    public DeploymentTimeoutException(String message) {
        super(message);
    }

    public DeploymentTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeploymentTimeoutException(Throwable cause) {
        super(cause);
    }
}
