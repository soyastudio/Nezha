package soya.framework.settler.component.bean;

import soya.framework.settler.ProcessException;
import soya.framework.settler.ProcessSession;

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
