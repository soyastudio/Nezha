package soya.framework.settler;

public interface ProcessorBuilder<T extends Processor> {
    T build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException;
}
