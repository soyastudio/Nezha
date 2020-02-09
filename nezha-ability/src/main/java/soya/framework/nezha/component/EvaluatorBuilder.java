package soya.framework.nezha.component;

import soya.framework.nezha.Evaluator;
import soya.framework.nezha.ProcessNode;
import soya.framework.nezha.ProcessSession;
import soya.framework.nezha.ProcessorBuildException;

public interface EvaluatorBuilder<T extends Evaluator>  {
    T build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException;
}
