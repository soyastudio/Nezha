package soya.framework.nezha;

public interface ProcessorBuilder<T extends Processor> {
    T build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException;
}
