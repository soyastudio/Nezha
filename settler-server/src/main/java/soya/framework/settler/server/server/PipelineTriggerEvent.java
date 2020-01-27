package soya.framework.settler.server.server;

import soya.framework.settler.DataObject;

public class PipelineTriggerEvent extends PipelineEvent {
    private DataObject dataObject;

    public PipelineTriggerEvent(String pipeline) {
        super(pipeline);
    }

    public PipelineTriggerEvent(String pipeline, DataObject dataObject) {
        super(pipeline);
        this.dataObject = dataObject;
    }

    public DataObject getDataObject() {
        return dataObject;
    }
}
