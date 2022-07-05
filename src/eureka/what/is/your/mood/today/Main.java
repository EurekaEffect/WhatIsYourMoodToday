package eureka.what.is.your.mood.today;

import eureka.what.is.your.mood.today.gui.Gui;
import eureka.what.is.your.mood.today.utils.Config;
import eureka.what.is.your.mood.today.utils.FileUtils;
import eureka.what.is.your.mood.today.utils.Handler;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

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

        Handler handler = config.load() != null ? config.load() : new Handler("emoji", null, new int[]{3, 3});
        new Gui(handler);
    }
}
