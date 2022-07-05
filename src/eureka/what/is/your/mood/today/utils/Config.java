package eureka.what.is.your.mood.today.utils;

import eureka.what.is.your.mood.today.Main;
import eureka.what.is.your.mood.today.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Config {
    private final File folder = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\WhatIsYourMoodToday");

    // All
    public void saveAll() {
        // Calendar
        save(Gui.applied ? (Main.imagePath + Main.SEPARATOR + Main.date) : "None");

        // Config
        Optional<Handler> saving = Gui.handlers.stream().filter(h -> h.item.isSelected()).findFirst();
        if (saving.isEmpty()) return;

        String path = saving.get().path;
        int[] data = saving.get().data;
        save(
                "path:" + path,
                "data.1:" + data[0],
                "data.2:" + data[1]
        );
    }

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

    public List<Integer> loadWindow() {
        try {
            return loadWindowData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean dayPassed(String data) {
        try {
            return !loadDate().equals(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<JButton> loadCalendarData() throws FileNotFoundException {
        assert false;

        if (!folder.exists()) folder.mkdir();
        File file = new File(folder.toPath() + "\\calendar.txt");
        if (!file.exists()) return null;
        List<JButton> buttons = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = br.lines().toList();

        for (String line : lines) {
            String[] split = line.split(Main.SEPARATOR);

            String path = split[0];
            String date = split[1];

            JButton button = FileUtils.createButton(new ImageIcon(path), null, true, false);
            button.setText(date);
            button.setVerticalTextPosition(SwingConstants.TOP);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setFont(new Font("Bryndan Write", Font.PLAIN, 28));
            buttons.add(button);
        }

        return buttons;
    }

    private List<Integer> loadWindowData() throws FileNotFoundException {
        if (!folder.exists()) folder.mkdir();

        File file = new File(folder.toPath() + "\\window.txt");
        if (!file.exists()) return List.of(50, 100, 700, 400);
        List<Integer> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        br.lines().forEach(line -> {
            int number = Integer.parseInt(line.substring(line.indexOf(":") + 1));

            if (line.startsWith("x")) list.add(number);
            if (line.startsWith("y")) list.add(number);
            if (line.startsWith("width")) list.add(number);
            if (line.startsWith("height")) list.add(number);
        });

        return list;
    }

    private String loadDate() throws FileNotFoundException {
        if (!folder.exists()) folder.mkdir();

        File file = new File(folder.toPath() + "\\date.txt");
        if (!file.exists()) return "None";

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.lines().toList().get(0);
        String split = line.substring(line.indexOf(":") + 1);

        return split;
    }

    private Handler loadArgs() throws Exception {
        if (!folder.exists()) folder.mkdir();

        File file = new File(folder.toPath() + "\\config.txt");
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

    public void saveWindow(int x, int y, int sizeX, int sizeY) {
        try {
            writeWindowData(x, y, sizeX, sizeY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDate(String data) {
        try {
            writeData(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeArgs(String... args) throws Exception {
        File file = new File(folder + "\\config.txt");

        if (!file.exists()) FileUtils.createFile(file.toPath());
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));

        for (String arg : args) {
            bf.write(arg + "\n");
        }

        bf.close();
    }

    private void writeCalendarData(String imagePath) throws IOException {
        File file = new File(folder + "\\calendar.txt");

        if (!file.exists()) FileUtils.createFile(file.toPath());
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = br.lines().toList();

        BufferedWriter bf = new BufferedWriter(new FileWriter(file));
        for (String line : lines) bf.write(line + "\n");

        if (!"None".equals(imagePath)) bf.write(imagePath + "\n");
        bf.close();
    }

    private void writeWindowData(int x, int y, int width, int height) throws IOException {
        File file = new File(folder + "\\window.txt");

        if (!file.exists()) FileUtils.createFile(file.toPath());
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));

        bf.write("x:" + x + "\n");
        bf.write("y:" + y + "\n");
        bf.write("width:" + width + "\n");
        bf.write("height:" + height + "\n");
        bf.close();
    }

    private void writeData(String date) throws IOException {
        File file = new File(folder + "\\date.txt");

        if (!file.exists()) FileUtils.createFile(file.toPath());
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));

        bf.write("date:" + date);
        bf.close();
    }
}
