package menu;

import frame.FrameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import main.MedicalGUI;

public class TransactionMenu extends BaseMenu {

    public TransactionMenu(MedicalGUI app) {
        super(app);
    }

    
    public void display() {
        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(FrameGUI.primaryColor);

        // Create the top panel with the logo
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(FrameGUI.secondaryColor);
        topPanel.setPreferredSize(new Dimension(app.getGui().getWidth(), 120));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding for the top panel

        // Load and resize the logo image
        ImageIcon logo = new ImageIcon("datafile/logo.jpg");
        Image resizedLogo = logo.getImage();
        resizedLogo = resizedLogo.getScaledInstance(369, 120, Image.SCALE_SMOOTH);
        logo.setImage(resizedLogo);

        // Add the logo to the top panel
        JLabel logoLabel = new JLabel(logo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(logoLabel, gbc);

        // Create the center panel for the buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(FrameGUI.primaryColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Padding for the center panel

        // Add a title label
        JLabel titleLabel = new JLabel("Transaction Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);

        // Add spacing below the title
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create and add buttons
        JButton visitButton = new JButton("Visit");
        setupButton(centerPanel, visitButton, e -> new VisitMenu(app).display());
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing between buttons

        JButton procedureButton = new JButton("Operate");
        setupButton(centerPanel, procedureButton, e -> new OperateMenu(app).display());
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing between buttons

        JButton backButton = new JButton("Back");
        setupButton(centerPanel, backButton, e -> new StartMenu(app).display());

        // Add panels to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Set up the main panel in the frame
        clearAndSetupPanel(mainPanel);
    }
}
