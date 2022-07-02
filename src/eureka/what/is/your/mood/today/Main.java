package eureka.what.is.your.mood.today;

import eureka.what.is.your.mood.today.gui.Gui;
import eureka.what.is.your.mood.today.utils.Config;
import eureka.what.is.your.mood.today.utils.Handler;

import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

public class Main {
    private static final Calendar c = Calendar.getInstance();
    public static final Config config = new Config();

    public static String imagePath = null;
    public static String date = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + c.get(Calendar.DAY_OF_MONTH);

    public static final String SEPARATOR = "@";

    public static void main(String[] args) {
        Handler handler = config.load() != null ? config.load() : new Handler("emoji", null, new int[]{3, 3});

        new Gui(handler);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Calendar
            config.save(Gui.applied ? (imagePath + SEPARATOR + date) : "None");

            // Config
            Optional<Handler> saving = Gui.handlers.stream().filter(h -> h.item.isSelected()).findFirst();
            if (saving.isEmpty()) return;

            String path = saving.get().path;
            int[] data = saving.get().data;
            config.save(
                    "path:" + path,
                    "data.1:" + data[0],
                    "data.2:" + data[1]
            );
        }));
    }
}
