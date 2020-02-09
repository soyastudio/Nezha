package soya.framework.nezha.component;

import soya.framework.nezha.DataObject;
import soya.framework.nezha.Processor;

public interface Renderer extends Processor {
    void render(DataObject data) throws RenderException;
}
