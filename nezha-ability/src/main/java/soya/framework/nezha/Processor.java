package soya.framework.nezha;

public interface Processor {
    void process(ProcessSession session) throws ProcessException;
}
