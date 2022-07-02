package eureka.what.is.your.mood.today.utils;

import javax.swing.*;

public class Handler {
    public String path;
    public JRadioButtonMenuItem item;
    public int[] data;

    public Handler(String path, JRadioButtonMenuItem item, int[] data) {
        this.path = path;
        this.item = item;
        this.data = data;
    }
}
