package eureka.what.is.your.mood.today.utils;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUtils {
    private static final File assets = new File(System.getProperty("user.home") + "\\AppData\\Roaming");
    private static final String githubAssets = "https://raw.githubusercontent.com/EurekaEffect/WhatIsYourMoodToday/main/src/";

    public static boolean hasAssetsFolder() {
        return new File(assets.toPath() + "\\assets").exists();
    }

    public static void downloadAssets() throws InterruptedException {
        //List<String> paths = List.of("assets", "assets/batcat/", "assets/cat/", "assets/cats/", "assets/emoji/", "assets/stripecat/");
        HashMap<String, Integer> paths = new HashMap<>();
        paths.put("assets/batcat/", 10);
        paths.put("assets/cat/", 6);
        paths.put("assets/cats/", 6);
        paths.put("assets/emoji/", 33);
        paths.put("assets/stripecat/", 20);

        System.out.println("Assets folder not found, downloading assets to " + assets.toPath().toString().concat("\\assets..."));

        File mainDirectory = new File(assets.toPath() + "/" + "assets");
        mainDirectory.mkdir();

        Thread thread = new Thread(() -> {
            for (String key : paths.keySet()) {
                int value = paths.get(key);

                File directory = new File(assets.toPath() + "/" + key);
                directory.mkdir();

                for (int i = 0; i < value; i++) {
                    String currentPath = githubAssets + key + i + ".png";

                    URL url;
                    try {
                        url = new URL(currentPath);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    File file = new File(assets.toPath() + "/" + key + i + ".png");

                    System.out.println(currentPath);
                    System.out.println(file);
                    download(url, file);
                }
            }
        });
        thread.start();
        thread.join();

        System.out.println("Downloading finished, reopening application...");
        // Running external jar, then closing current jar
    }

    public static void download(URL url, File file) {
        try {
            InputStream input = url.openStream();
            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }

            input.close();
            output.close();
        } catch (Exception ignored) {
        }
    }

    public static List<File> getAssets(String path) {
        File file = new File(assets.toPath() + "\\assets\\" + path);

        return Arrays.stream(Objects.requireNonNull(file.listFiles())).toList();
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
