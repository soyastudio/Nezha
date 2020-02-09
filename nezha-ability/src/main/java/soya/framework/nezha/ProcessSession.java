package soya.framework.nezha;

public interface ProcessSession {
    ProcessContext getContext();

    DataObject current();

    void update(DataObject dataObject);
}
