package soya.framework.nezha.support;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.ClassPath;
import soya.framework.nezha.Component;
import soya.framework.nezha.Processor;
import soya.framework.nezha.ProcessorBuildException;
import soya.framework.nezha.ProcessorBuilder;

import java.io.IOException;
import java.util.*;

class Components {
    private static ImmutableMap<String, ProcessorBuilder> builders;
    private static Set<String> registeredPackages = new LinkedHashSet<>();
    private static Set<PackageRegistrationListener> registrationListeners = new HashSet<>();

    static {
        String defaultPackage = Processor.class.getPackage().getName();
        registeredPackages.add(defaultPackage);
        register(defaultPackage);
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
            if(builder instanceof PackageRegistrationListener) {
                registrationListeners.add((PackageRegistrationListener) builder);
            }
            map.put(name, builder);
        });

        builders = ImmutableMap.copyOf(map);

        registrationListeners.forEach(e -> {
            e.packageRegistered(packageName);
        });
    }

    public static String[] getRegisteredPackages() {
        return registeredPackages.toArray(new String[registeredPackages.size()]);
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
