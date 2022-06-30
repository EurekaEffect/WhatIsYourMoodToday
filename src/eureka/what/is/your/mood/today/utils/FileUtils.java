package eureka.what.is.your.mood.today.utils;

import eureka.what.is.your.mood.today.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    public static List<File> getAssets() {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource("assets");

        try {
            return Files.walk(Paths.get(resource.toURI())).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean corrupted(File file) {
        try {
            return !file.exists() || Files.size(file.toPath()) == 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
