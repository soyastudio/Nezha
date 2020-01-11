package soya.framework.settler;

public interface EvaluatorBuilder<T extends Evaluator> {
    T build(EvaluateTreeNode[] arguments, EvaluationContext context) throws EvaluatorBuildException;
}
