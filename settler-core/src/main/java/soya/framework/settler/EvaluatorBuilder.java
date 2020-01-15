package soya.framework.settler;

public interface EvaluatorBuilder<T extends Evaluator> extends ProcessorBuilder<T> {
    T build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException;
}
