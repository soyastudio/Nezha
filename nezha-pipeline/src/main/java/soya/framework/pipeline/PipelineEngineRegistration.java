package soya.framework.pipeline;

import java.util.Iterator;
import java.util.Map;

public interface PipelineEngineRegistration {
    Iterator<Map.Entry<String, PipelineEngineRegister>> registers();
}
