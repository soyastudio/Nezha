package soya.framework.markdown;

import com.google.common.eventbus.EventBus;

public class Markdown {
    private static Markdown instance;

    private EventBus eventBus = new EventBus("Markdown");

    static {
        instance = new Markdown();
    }

    private Markdown() {

    }


}
