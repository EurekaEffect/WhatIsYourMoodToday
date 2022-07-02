package eureka.what.is.your.mood.today.gui;

import eureka.what.is.your.mood.today.Main;
import eureka.what.is.your.mood.today.utils.FileUtils;
import eureka.what.is.your.mood.today.utils.Handler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Gui extends JFrame {
    private final List<JButton> buttons = new ArrayList<>();
    private JButton chosenButton = null;

    private JMenuBar jMenuBar = new JMenuBar();
    private JMenu jMenu = new JMenu("Presets");

    private JButton calendar;
    private JButton apply;

    private final ButtonGroup group = new ButtonGroup();
    public static final List<Handler> handlers = List.of(
            new Handler("batcat", new JRadioButtonMenuItem("Bat Cat"), new int[]{5, 2}),
            new Handler("cat", new JRadioButtonMenuItem("Cat"), new int[]{3, 2}),
            new Handler("cats", new JRadioButtonMenuItem("Cats"), new int[]{3, 2}),
            new Handler("emoji", new JRadioButtonMenuItem("Emoji"), new int[]{3, 3}),
            new Handler("stripecat", new JRadioButtonMenuItem("Stripe Cat"), new int[]{5, 4})
    );

    private final Color background = new Color(130, 160, 227);
    public static boolean applied = false;

    public Gui(Handler loading) {
        this.setTitle("What is your mood today?");

        this.setBounds(50, 100, 700, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(loading.data[0], loading.data[1]));
        this.getContentPane().setBackground(background);

        List<File> files = FileUtils.getAssets(loading.path);

        files.stream()
                .filter(file -> file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))
                .filter(file -> !FileUtils.corrupted(file))
                .forEach(file -> {
                    String path = file.getPath();

                    ImageIcon image = new ImageIcon(path);
                    JButton button = new JButton(image);
                    button.setBorder(null);
                    button.setBackground(background);
                    button.setFocusable(false);
                    this.add(button);

                    buttons.add(button);
                });


        buttons.forEach(button -> button.addActionListener(action -> {
            if (applied) return;
            if (chosenButton != null) chosenButton.setBorder(null);

            chosenButton = button;
            chosenButton.setBorder(new RoundedBorder(Color.BLUE, 100));
            Main.imagePath = String.valueOf(chosenButton.getIcon());
        }));

        calendar = FileUtils.createButton("Calendar", () -> new Calendar(this.getX(), this.getY()), true);
        apply = FileUtils.createButton("Apply", () -> {
            if (chosenButton == null) return;

            apply.setText("Applied");
            applied = true;
        }, true);
        handlers.forEach(handler -> {
            handler.item.setSelected(Objects.equals(handler.path, loading.path));

            group.add(handler.item);
            jMenu.add(handler.item);
        });
        jMenuBar.add(jMenu);
        jMenuBar.add(calendar);
        jMenuBar.add(apply);

        this.setJMenuBar(jMenuBar);
        this.setVisible(true);
    }
}
