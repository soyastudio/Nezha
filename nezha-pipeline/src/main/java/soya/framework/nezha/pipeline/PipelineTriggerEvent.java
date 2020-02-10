package soya.framework.nezha.pipeline;

import soya.framework.nezha.DataObject;

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
