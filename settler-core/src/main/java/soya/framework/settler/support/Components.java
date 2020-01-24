package soya.framework.settler.support;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.ClassPath;
import soya.framework.settler.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Components {
    private static ImmutableMap<String, ProcessorBuilder> builders;

    static {
        Components.register(Processor.class.getPackage().getName());
    }

    public static void register(String... packageName) {
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

    public static Processor create(ExecutableNode node, ProcessContext context) {
        Processor processor = null;
        if(node instanceof FunctionNode) {
            FunctionNode functionNode = (FunctionNode) node;

        }
        return processor;
    }

    private Processor fromFunctionNode(FunctionNode functionNode, ProcessContext context) {
        ProcessorBuilder builder = getProcessBuilder(functionNode.getName());

        return builder.build(functionNode.getArguments(), context);
    }

    public static ProcessorBuilder getProcessBuilder(String functionName) {
        return builders.get(functionName);
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
