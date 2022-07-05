package eureka.what.is.your.mood.today.utils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileUtils {
    private static final File pcAssets = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\WhatIsYourMoodToday");
    private static final String githubAssets = "https://raw.githubusercontent.com/EurekaEffect/WhatIsYourMoodToday/main/src/";
    public static String launcher = pcAssets.toPath() + "/assets/launcher/Launcher.jar";

    public static boolean hasAssetsFolder() {
        return pcAssets.exists();
    }

    public static void downloadAssets() throws InterruptedException {
        HashMap<String, Integer> paths = new HashMap<>();
        paths.put("assets/batcat/", 10);
        paths.put("assets/cat/", 6);
        paths.put("assets/cats/", 6);
        paths.put("assets/emoji/", 33);
        paths.put("assets/stripecat/", 20);
        paths.put("assets/launcher", 0);

        pcAssets.mkdir();
        File mainDirectory = new File(pcAssets.toPath() + "/" + "assets");
        mainDirectory.mkdir();

        Thread thread = new Thread(() -> {
            for (String key : paths.keySet()) {
                int value = paths.get(key);

                File directory = new File(pcAssets.toPath() + "/" + key);
                directory.mkdir();

                for (int i = 0; i < value; i++) {
                    String currentPath = githubAssets + key + i + ".png";

                    URL url = urlOf(currentPath);
                    File file = new File(pcAssets.toPath() + "/" + key + i + ".png");
                    download(url, file);
                }
            }

            String launcherPath = githubAssets + "/assets/launcher/Launcher.jar";
            URL url = urlOf(launcherPath);
            File file = new File(pcAssets.toPath() + launcherPath.replace(githubAssets, ""));

            download(url, file);
        });
        thread.start();
        thread.join();
    }

    private static URL urlOf(String path) {
        try {
            return new URL(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void announce(String text) {
        new Thread(() -> JOptionPane.showMessageDialog(null, text)).start();
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
        File file = new File(pcAssets.toPath() + "\\assets\\" + path);

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

    public static JButton createButton(Object object, Runnable actionListener, boolean transparent, boolean mouseListener) {
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

        return mouseListener ? mouseListener(button) : button;
    }

    public static JButton mouseListener(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //jMenu.setBorder(BorderFactory.createEtchedBorder());
                button.setBorderPainted(true);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
                super.mouseExited(e);
            }
        });

        return button;
    }
}
