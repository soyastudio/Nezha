package soya.framework.nezha.component.bean;

import soya.framework.nezha.ProcessException;
import soya.framework.nezha.ProcessSession;

public class EchoBean extends ProcessBean {
    private String message;

    @Override
    public void process(ProcessSession session) throws ProcessException {
        if (message == null) {
            System.out.println("---------------- Echo...");

        } else {
            System.out.println("---------------- Echo: " + message);

        }
    }
}
