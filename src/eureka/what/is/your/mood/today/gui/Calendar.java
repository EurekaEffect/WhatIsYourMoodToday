package eureka.what.is.your.mood.today.gui;

import eureka.what.is.your.mood.today.Main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Calendar extends JFrame {
    private final Color background = new Color(130, 160, 227);

    public Calendar(int x, int y) {
        this.setTitle("Calendar");

        this.setBounds(x, y, 700, 400);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.getContentPane().setBackground(background);

        List<JButton> buttons = Main.config.loadCalendar();
        buttons.forEach(this::add);

        this.setVisible(true);
    }
}
