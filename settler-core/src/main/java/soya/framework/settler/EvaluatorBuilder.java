package soya.framework.settler;

public interface EvaluatorBuilder<T extends Evaluator>  {
    T build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException;
}
