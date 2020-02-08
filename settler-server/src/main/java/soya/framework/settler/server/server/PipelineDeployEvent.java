package soya.framework.settler.server.server;

import com.google.common.io.Files;

import java.io.File;

public class PipelineDeployEvent extends ServiceEvent {

    public static final String[] DEPLOYMENTS = new String[] {".zip"};
    public static final String START_EXTENSION = "start";
    public static final String STOP_EXTENSION = "stop";
    public static final String DELETE_EXTENSION = "delete";

    private final String pipeline;
    private final DeployEventType eventType;

    public PipelineDeployEvent(String pipeline, DeployEventType eventType) {
        this.pipeline = pipeline;
        this.eventType = eventType;
    }

    public String getPipeline() {
        return pipeline;
    }

    public DeployEventType getEventType() {
        return eventType;
    }

    public static enum DeployEventType {
        DEPLOY, UNDEPLOY, SUCCESS, FAILURE
    }
}
