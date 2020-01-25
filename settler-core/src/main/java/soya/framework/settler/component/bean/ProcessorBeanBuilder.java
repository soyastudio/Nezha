package soya.framework.settler.component.bean;

import com.google.gson.Gson;
import soya.framework.settler.*;
import soya.framework.settler.support.ProcessorBuilderSupport;

import java.lang.reflect.Type;

@Component(name = "process")
public class ProcessorBeanBuilder extends ProcessorBuilderSupport<Processor> {

    @Override
    public Processor build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException {
        if(arguments == null || arguments.length == 0) {
            throw new ProcessorBuildException("Processor is not defined.");
        }

        Processor processor = null;

        ProcessNode pNode = arguments[0];
        if(pNode instanceof ExecutableNode) {
            // TODO:

        } else {
            AssignmentNode assignmentNode = (AssignmentNode) pNode;
            try {
                Class<?> cls = Class.forName(assignmentNode.getStringValue(context));
                if(arguments.length > 1) {
                    AssignmentNode params = (AssignmentNode) arguments[1];
                    if(params.isJsonObject()) {
                        processor = new Gson().fromJson(params.getStringValue(context), (Type) cls);
                    }

                } else {
                    processor = (Processor) cls.newInstance();
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new ProcessorBuildException(e);
            }
        }

        return processor;
    }
}
