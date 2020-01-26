package soya.framework.settler.component;

import soya.framework.settler.Evaluator;
import soya.framework.settler.ProcessNode;
import soya.framework.settler.ProcessSession;
import soya.framework.settler.ProcessorBuildException;

public interface EvaluatorBuilder<T extends Evaluator>  {
    T build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException;
}
