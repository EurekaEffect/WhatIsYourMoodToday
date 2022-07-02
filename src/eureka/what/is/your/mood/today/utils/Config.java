package eureka.what.is.your.mood.today.utils;

import eureka.what.is.your.mood.today.Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Config {
    private final String config = System.getProperty("user.home") + "\\AppData\\Roaming\\WIYMT.txt";
    private final String calendar = System.getProperty("user.home") + "\\AppData\\Roaming\\calendar.txt";

    // Loading
    public Handler load() {
        try {
            return loadArgs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<JButton> loadCalendar() {
        try {
            return loadCalendarData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<JButton> loadCalendarData() throws FileNotFoundException {
        assert false;

        File file = new File(calendar);
        if (!file.exists()) return null;
        List<JButton> buttons = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = br.lines().toList();

        for (String line : lines) {
            String[] split = line.split(Main.SEPARATOR);

            String path = split[0];
            String date = split[1];

            JButton button = FileUtils.createButton(new ImageIcon(path), null, true);
            button.setText(date);
            button.setVerticalTextPosition(SwingConstants.TOP);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setFont(new Font("Bryndan Write", Font.PLAIN, 28));
            buttons.add(button);
        }

        return buttons;
    }

    private Handler loadArgs() throws Exception {
        File file = new File(config);
        if (!file.exists()) return null;
        AtomicReference<String> path = new AtomicReference<>();
        AtomicReference<Integer> d1 = new AtomicReference<>(3);
        AtomicReference<Integer> d2 = new AtomicReference<>(3);

        BufferedReader br = new BufferedReader(new FileReader(file));
        br.lines().forEach(line -> {
            String p = line.substring(line.indexOf(":") + 1);

            if (line.startsWith("path")) path.set(p);
            if (line.startsWith("data")) {
                Integer i = Integer.valueOf(p);

                if (line.contains(".1")) d1.set(i);
                if (line.contains(".2")) d1.set(i);
            }
        });

        return new Handler(path.get(), null, new int[]{d1.get(), d2.get()});
    }

    // Saving
    public void save(String... args) {
        try {
            writeArgs(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(String imagePath) {
        try {
            writeCalendarData(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeArgs(String... args) throws Exception {
        File file = new File(config);

        if (!file.exists()) FileUtils.createFile(file.toPath());
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));

        for (String arg : args) {
            bf.write(arg + "\n");
        }

        bf.close();
    }

    private void writeCalendarData(String imagePath) throws IOException {
        File file = new File(calendar);

        if (!file.exists()) FileUtils.createFile(file.toPath());
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = br.lines().toList();

        BufferedWriter bf = new BufferedWriter(new FileWriter(file));
        for (String line : lines) bf.write(line + "\n");

        if (!"None".equals(imagePath)) bf.write(imagePath + "\n");
        bf.close();
    }
}
