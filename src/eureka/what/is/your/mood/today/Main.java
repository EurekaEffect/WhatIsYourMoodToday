package eureka.what.is.your.mood.today;

import eureka.what.is.your.mood.today.gui.Gui;
import eureka.what.is.your.mood.today.utils.Config;
import eureka.what.is.your.mood.today.utils.FileUtils;
import eureka.what.is.your.mood.today.utils.Handler;

import javax.swing.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Main {
    private static final Calendar calendar = Calendar.getInstance();
    public static final Config config = new Config();

    public static String imagePath = null;
    public static String date = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + calendar.get(Calendar.DAY_OF_MONTH);

    public static final String SEPARATOR = "@";

    public static void main(String[] args) throws InterruptedException, IOException {
        if (!FileUtils.hasAssetsFolder()) {
            FileUtils.downloadAssets();

            // Reopening application
            Process process = Runtime.getRuntime().exec("java -jar " + FileUtils.launcher + " " + System.getProperty("java.class.path"));
            process.waitFor();
            return;
        }

        List<Integer> window = config.loadWindow();
        Handler handler = config.load() != null ? config.load() : new Handler("emoji", null, new int[]{3, 3});
        boolean dayPassed = config.dayPassed(date);

        Gui gui = new Gui(handler, window, dayPassed);
        if (!dayPassed) JOptionPane.showMessageDialog(null, "The day has not passed, come back tomorrow to leave your new mood :).", "Calendar", JOptionPane.INFORMATION_MESSAGE);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            config.saveWindow(gui.getX(), gui.getY(), gui.getWidth(), gui.getHeight());
            if (Gui.applied) config.saveDate(date);
        }));
    }
}
