package soya.framework.settler;

public interface Processor {
    void process(ProcessSession session) throws ProcessException;
}
