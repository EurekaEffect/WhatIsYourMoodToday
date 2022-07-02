package eureka.what.is.your.mood.today.utils;

import eureka.what.is.your.mood.today.Main;

import javax.swing.*;
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
    public static List<File> getAssets(String path) {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource("assets/" + path);

        try {
            return Files.walk(Paths.get(resource.toURI()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
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

    public static void createFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JButton createButton(Object object, Runnable actionListener, boolean transparent) {
        JButton button = null;

        if (object instanceof String name) button = new JButton(name);
        if (object instanceof ImageIcon imageIcon) button = new JButton(imageIcon);
        if (button == null) return null;

        if (transparent) {
            // Transparent
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusable(false);
        }

        if (actionListener != null) button.addActionListener(action -> actionListener.run());

        return button;
    }
}
