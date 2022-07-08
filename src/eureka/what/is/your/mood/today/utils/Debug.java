package eureka.what.is.your.mood.today.utils;

import java.util.ArrayList;
import java.util.List;

public class Debug {
    protected List<String> pushed = new ArrayList<>();

    protected void debug(String text, Class<? extends Debug> clazz) {
        String state = pushed(text) ? "pop " : "pushed ";

        System.out.println("[" + clazz.getSimpleName() + "] " + state + text);
    }

    protected void push(String text) { pushed.add(text);}
    protected boolean pushed(String text) {
        return pushed.contains(text);
    }
}
