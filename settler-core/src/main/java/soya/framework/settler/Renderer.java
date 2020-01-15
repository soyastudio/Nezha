package soya.framework.settler;

@ComponentType
public interface Renderer extends Processor {
    void render(String data) throws RenderException;
}
