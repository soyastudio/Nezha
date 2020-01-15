package soya.framework.settler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkflowEngine extends Components {
    protected static WorkflowEngine instance;

    private ProcessContext context;
    private ExecutorService executorService;

    static {
        instance = new WorkflowEngine();
        register(Processor.class.getPackage().getName());
    }

    private WorkflowEngine(ProcessContext context, ExecutorService executorService) {
        this.context = context;
        this.executorService = executorService;
    }

    protected WorkflowEngine() {
        this.context = new DefaultProcessContext();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public String evaluate(String data, String expression) {
        String result = data;
        FunctionNode[] functions = FunctionNode.toFunctions(expression);
        for (FunctionNode function : functions) {
            Processor processor = create(function, context);
            Method method = getProcessMethod(processor.getClass());
            Object[] params = new Object[0];
            if (method.getParameterCount() == 1) {
                params = new Object[]{result};
            }

            try {
                result = (String) method.invoke(processor, params);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static WorkflowEngine getInstance() {
        return instance;
    }

    static class DefaultProcessContext implements ProcessContext {
        private Properties configuration;

        public DefaultProcessContext() {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = WorkflowEngine.class.getClassLoader();
            }

            configuration = new Properties();
            InputStream inputStream = classLoader.getResourceAsStream("settler-config.properties");
            if (inputStream != null) {
                try {
                    configuration.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
