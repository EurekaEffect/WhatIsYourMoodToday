package eureka.what.is.your.mood.today.gui;

import eureka.what.is.your.mood.today.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class Gui extends JFrame {

    public Gui() {
        this.setTitle("What is your mood today?");

        this.setBounds(50, 100, 700, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 3));

        List<File> files = FileUtils.getAssets();

        files.stream()
                .filter(file -> file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))
                .filter(file -> !FileUtils.corrupted(file))
                .forEach(file -> {
                    String path = file.getPath();

                    ImageIcon image = new ImageIcon(path);
                    JButton button = new JButton(image);
                    button.setBorder(null);
                    button.setBackground(new Color(130, 160, 227));
                    button.setFocusable(false);
                    this.add(button);
                });

        this.setVisible(true);
    }
}
