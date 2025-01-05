package frame;

import javax.swing.*;
import java.awt.*;

public class FrameGUI extends JFrame {
    Dimension preferredSize = new Dimension(800, 600);
    ImageIcon logo = new ImageIcon("datafile/logo.jpg");

    // Define variables as public static
    public static int buttonWidth = 300;
    public static int buttonHeight = 60;
    public static int smallButtonWidth = 100;
    public static int smallButtonHeight = 30;
    public static int xMargin;
    public static int yMargin = 20;
    public static int titleSize = 25;
    public static int fontSize = 17;
    public static Color primaryColor = new Color(0xD0D4CA);
    public static Color secondaryColor = new Color(0xB6BAB2);
    public static Color lightPrimaryColor = new Color(0xC7CBC2);

    public FrameGUI() {
        this.setVisible(true);
        this.setTitle("Help Health Management Service");
        this.setSize(preferredSize);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(new Color(0xD0D4CA));
        xMargin = (this.getWidth() / 2); // Initialize xMargin based on the frame width
    }
}
