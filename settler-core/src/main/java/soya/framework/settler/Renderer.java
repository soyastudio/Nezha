package soya.framework.settler;

@ComponentType
public interface Renderer extends Processor {
    void render(DataObject data) throws RenderException;
}
