package soya.framework.settler;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.ClassPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Components {

    private static ImmutableMap<String, ProcessorBuilder> builders;

    protected static void register(String... packageName) {
        Map<String, ProcessorBuilder> map = new HashMap<>();
        if (builders != null) {
            map.putAll(builders);
        }

        Set<Class<?>> set = findByAnnotation(packageName);
        set.forEach(e -> {
            Component def = e.getAnnotation(Component.class);
            String name = def.name();
            ProcessorBuilder builder = newInstance(e);
            map.put(name, builder);

        });

        builders = ImmutableMap.copyOf(map);
    }

    public static Method getProcessMethod(Class<? extends Processor> type) {
        Class<?>[] interfaces = type.getInterfaces();
        for(Class<?> intf: interfaces) {
            if(intf.getAnnotation(ComponentType.class) != null) {
                return intf.getMethods()[0];
            }
        }
        return null;
    }

    public static Processor create(FunctionNode function, ProcessSession session) {
        return builders.get(function.getName()).build(function.getArguments(), session);
    }

    public static String toJson(ProcessNode... nodes) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(nodes);
    }

    private static ProcessorBuilder newInstance(Class<?> clazz) throws ProcessorBuildException {
        try {
            return (ProcessorBuilder) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ProcessorBuildException(e);
        }
    }

    private static Set<Class<?>> findByAnnotation(String... packageName) {
        Set<Class<?>> set = new HashSet<>();
        try {
            ClassPath classpath = ClassPath.from(getClassLoader());
            for (String pkg : packageName) {
                Set<ClassPath.ClassInfo> results = classpath.getTopLevelClassesRecursive(pkg);
                results.forEach(e -> {
                    Class<?> cls = e.load();
                    if (cls.getAnnotation(Component.class) != null) {
                        set.add(cls);
                    }
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return set;
    }

    private static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Components.class.getClassLoader();
        }

        return classLoader;
    }
}
