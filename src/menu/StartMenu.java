package menu;

import frame.FrameGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import main.MedicalGUI;

public class StartMenu extends BaseMenu {

    public StartMenu(MedicalGUI app) {
        super(app);
    }

    public void display() {
        // Remove all components from the frame
        app.getGui().getContentPane().removeAll();

        // Load and resize the logo image
        ImageIcon logo = new ImageIcon("datafile/logo.jpg");
        Image resizedLogo = logo.getImage();
        resizedLogo = resizedLogo.getScaledInstance(369, 120, Image.SCALE_SMOOTH);
        logo.setImage(resizedLogo);

        // Initialize JLabel for the logo
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(logo);

        // Initialize the panels
        JPanel topPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Vertical alignment for buttons

        // Set the background colors for the panels
        topPanel.setBackground(FrameGUI.secondaryColor);
        centerPanel.setBackground(FrameGUI.primaryColor);

        // Add padding to the panels
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the top panel
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Padding around the center panel

        // Add the logo to the top panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(logoLabel, gbc);

        // Create and add buttons to the center panel using BaseMenu methods
        JButton objectManagementButton = new JButton("Object Management");
        setupButton(centerPanel, objectManagementButton, e -> new ObjectManagementMenu(app).display());

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Add spacing between buttons

        JButton transactionButton = new JButton("Transaction");
        setupButton(centerPanel, transactionButton, e -> new TransactionMenu(app).display());

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Add spacing between buttons

        JButton exitButton = new JButton("Exit");
        setupButton(centerPanel, exitButton, e -> System.exit(0));

        // Add the panels to the frame
        app.getGui().getContentPane().add(topPanel, BorderLayout.NORTH);
        app.getGui().getContentPane().add(centerPanel, BorderLayout.CENTER);
        app.getGui().revalidate();
        app.getGui().repaint();
    }
}
