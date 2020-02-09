package soya.framework.nezha.support;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import soya.framework.nezha.*;

import java.lang.reflect.ParameterizedType;

public abstract class ProcessorBuilderSupport<T extends Processor> implements ProcessorBuilder<T> {

    protected Gson gson;
    protected Class<T> clazz;
    protected String[] properties;

    public ProcessorBuilderSupport() {
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Component def = getClass().getAnnotation(Component.class);
        this.properties = def.arguments();
    }

    public T create(String[] arguments, ProcessContext context) throws ProcessorBuildException {
        try {
            T t = gson.fromJson(toJsonObject(clazz, properties, arguments, context), clazz);
            return t;
        } catch (NoSuchFieldException e) {
            throw new ProcessorBuildException(e);
        }
    }

    public Class<T> getProcessorType() {
        return clazz;
    }

    public Class<?> getInnerProcessorType() {
        Class<?>[] inners = getProcessorType().getDeclaredClasses();
        if (inners != null) {
            for (Class<?> c : inners) {
                if (Evaluator.class.isAssignableFrom(c)) {
                    return c;
                }
            }
        }

        return null;
    }

    protected JsonObject toJsonObject(Class<?> clazz, String[] properties, String[] arguments, ProcessContext context) throws NoSuchFieldException {
        JsonObject json = new JsonObject();
        int len = arguments.length;
        for (int i = 0; i < properties.length; i++) {
            if (i < len) {
                String property = properties[i];
                Class<?> type = getPropertyType(clazz, property);


            } else {
                break;
            }
        }

        return json;
    }

    protected Class<?> getPropertyType(Class<?> clazz, String property) throws NoSuchFieldException {
        return clazz.getDeclaredField(property).getType();
    }
}
