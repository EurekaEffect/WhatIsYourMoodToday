package eureka.what.is.your.mood.today.utils;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    private static final File assets = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\assets");

    public static boolean hasAssetsFolder() {
        return assets.exists();
    }

    public static void downloadAssets() {
        assets.mkdir();
        for (String path : List.of("batcat", "cat", "cats", "emoji", "stripecat")) {
            new File(assets.toPath() + "\\" + path).mkdir();
        }
    }

    public static List<File> getAssets(String path) {
        List<File> files = new ArrayList<>();



        return files;
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
