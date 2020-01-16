package soya.framework.settler.evaluators.utils;

import soya.framework.settler.*;

@Component(name = "sub_string")
public class SubStringBuilder implements EvaluatorBuilder<SubStringBuilder.SubString> {

    @Override
    public SubString build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        SubString subString = new SubString();
        if (arguments.length == 1) {
            AssignmentNode parameter = (AssignmentNode) arguments[0];
            subString.end = parameter.getInteger(session.getContext());

        } else if (arguments.length == 2) {
            AssignmentNode p0 = (AssignmentNode) arguments[0];
            AssignmentNode p1 = (AssignmentNode) arguments[1];
            subString.start = p0.getInteger(session.getContext());
            subString.end = p1.getInteger(session.getContext());

        } else {
            throw new IllegalFunctionArgumentException();
        }

        return subString;
    }

    static class SubString implements Evaluator {
        private int start = -1;
        private int end;

        private SubString() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            if (data == null) {
                return null;
            }

            if (start >= 0 && end >= start) {
                if (data.length() < start) {
                    return null;
                } else if (data.length() < end) {
                    return data.substring(start, data.length());
                } else {
                    return data.substring(start, end);
                }
            } else if (start < 0 && end > 0) {
                if(data.length() > end) {
                    return null;
                } else {
                    return data.substring(end);
                }
            } else {
                return null;
            }
        }
    }
}
