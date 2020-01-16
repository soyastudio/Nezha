package soya.framework.settler;

import java.util.List;

public interface Workflow {
    String getName();

    ProcessContext getContext();

    List<ExecutableNode> getTasks();

}
