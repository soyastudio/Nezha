package soya.framework.settler;

public interface ProcessSession {
    ProcessContext getContext();

    DataObject current();

    void update(DataObject dataObject);
}
