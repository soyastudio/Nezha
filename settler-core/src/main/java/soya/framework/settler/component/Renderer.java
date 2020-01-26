package soya.framework.settler.component;

import soya.framework.settler.DataObject;
import soya.framework.settler.Processor;

public interface Renderer extends Processor {
    void render(DataObject data) throws RenderException;
}
