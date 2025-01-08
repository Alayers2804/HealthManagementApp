package menu;

import frame.FrameGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import main.MedicalGUI;

public abstract class BaseMenu {

    protected MedicalGUI app;

    public BaseMenu(MedicalGUI app) {
        this.app = app;
    }

    protected void clearAndSetupPanel(JPanel panel) {
        app.getGui().getContentPane().removeAll();
        app.getGui().getContentPane().add(panel);
        app.getGui().revalidate();
        app.getGui().repaint();
    }

    protected void addLabel(JPanel panel, String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        panel.add(label);
    }

    protected void addButton(JPanel panel, JButton button, int x, int y, int width, int height, ActionListener action) {
        button.setBounds(x, y, width, height);
        button.addActionListener(action);
        panel.add(button);
    }

    protected void setupButton(JPanel panel, JButton button, ActionListener action) {
        // Set up button properties
        button.setFocusable(false);
        button.setFont(new Font("Consolas", Font.BOLD, FrameGUI.fontSize));
        button.setPreferredSize(new Dimension(FrameGUI.buttonWidth, FrameGUI.buttonHeight)); // Fixed button size
        button.setMaximumSize(new Dimension(FrameGUI.buttonWidth, FrameGUI.buttonHeight)); // Prevent resizing

        // Add action listener
        button.addActionListener(action);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);
    }

}
