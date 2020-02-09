package soya.framework.nezha;

import java.util.List;

public interface Workflow {
    String getName();

    ProcessContext getContext();

    List<ExecutableNode> getTasks();

}
