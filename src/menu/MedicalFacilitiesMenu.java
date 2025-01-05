package menu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import main.HelpHealthManagementApp;
import menu.ObjectManagementMenu;
import object.Hospital;
import object.Clinic;
import manager.MedicalFacilitiesManager; // Import the MedicalFacilitiesManager
import object.MedicalFacility;

public class MedicalFacilitiesMenu extends BaseMenu {

    private JTable hospitalTable;
    private DefaultTableModel hospitalTableModel;
    private MedicalFacilitiesManager facilitiesManager; // Use MedicalFacilitiesManager

    private JTable clinicTable;
    private DefaultTableModel clinicTableModel;

    public MedicalFacilitiesMenu(HelpHealthManagementApp app) {
        super(app);
        facilitiesManager = new MedicalFacilitiesManager(); // Initialize the MedicalFacilitiesManager
    }

    public void display() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment
        panel.setBackground(Color.LIGHT_GRAY);

        // Create components
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Hospital", createHospitalPanel());
        tabbedPane.addTab("Clinic", createClinicPanel());

        panel.add(tabbedPane);
        clearAndSetupPanel(panel);
        loadHospitals();
        loadClinics();
    }

    private JPanel createHospitalPanel() {
        JPanel hospitalPanel = new JPanel();
        GroupLayout layout = new GroupLayout(hospitalPanel);
        hospitalPanel.setLayout(layout);
        hospitalPanel.setBackground(Color.LIGHT_GRAY);

        JLabel sortLabel = new JLabel("Sorted by:");
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Name (A-Z)", "Name (Z-A)", "ID (Ascending)", "ID (Descending)"});

        hospitalTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Admission Probability"}, 0);
        hospitalTable = new JTable(hospitalTableModel);
        JScrollPane scrollPane = new JScrollPane(hospitalTable);

        JButton addButton = new JButton("Add Hospital");
        addButton.addActionListener(e -> addHospital());
        JButton deleteButton = new JButton("Delete Hospital");
        deleteButton.addActionListener(e -> deleteHospital());
        JButton editButton = new JButton("Edit Hospital");
        editButton.addActionListener(e -> editHospital());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> new ObjectManagementMenu(app).display());

        // Set GroupLayout properties
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(sortLabel)
                                .addComponent(sortComboBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        )
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(editButton)
                                .addComponent(backButton)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(sortLabel)
                                .addComponent(sortComboBox)
                        )
                        .addGap(10)
                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(editButton)
                                .addComponent(backButton)
                        )
        );

        return hospitalPanel;
    }

    private JPanel createClinicPanel() {
        JPanel clinicPanel = new JPanel();
        GroupLayout layout = new GroupLayout(clinicPanel);
        clinicPanel.setLayout(layout);
        clinicPanel.setBackground(Color.LIGHT_GRAY);

        JLabel sortLabel = new JLabel("Sorted by:");
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Name (A-Z)", "Name (Z-A)", "ID (Ascending)", "ID (Descending)"});

        clinicTableModel = new DefaultTableModel(new Object[]{"ID", "Clinic Name", "Fee", "Gap Percent"}, 0);
        clinicTable = new JTable(clinicTableModel);
        JScrollPane scrollPane = new JScrollPane(clinicTable);

        JButton addButton = new JButton("Add Clinic");
        addButton.addActionListener(e -> addClinic());
        JButton deleteButton = new JButton("Delete Clinic");
        deleteButton.addActionListener(e -> deleteClinic());
        JButton editButton = new JButton("Edit Clinic");
        editButton.addActionListener(e -> editClinic());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> new ObjectManagementMenu(app).display());

        // Set GroupLayout properties
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(sortLabel)
                                .addComponent(sortComboBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        )
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(editButton)
                                .addComponent(backButton)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(sortLabel)
                                .addComponent(sortComboBox)
                        )
                        .addGap(10)
                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(editButton)
                                .addComponent(backButton)
                        )
        );

        return clinicPanel;
    }

    private void addHospital() {
        String name = JOptionPane.showInputDialog("Enter Hospital Name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Hospital name cannot be empty.");
            return;
        }
        String probStr = JOptionPane.showInputDialog("Enter Admission Probability (0-1):");
        try {
            double probAdmit = Double.parseDouble(probStr);
            if (probAdmit < 0 || probAdmit > 1) {
                throw new NumberFormatException();
            }
            Hospital newHospital = new Hospital(name, probAdmit);
            facilitiesManager.addHospital(newHospital); // Add to manager
            loadHospitals(); // Refresh the table
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid admission probability. Please enter a number between 0 and 1.");
        }
    }

    private void addClinic() {
        String name = JOptionPane.showInputDialog("Enter Clinic Name:");
        String feeStr = JOptionPane.showInputDialog("Enter Consultation Fee:");
        double fee = Double.parseDouble(feeStr);
        String gapStr = JOptionPane.showInputDialog("Enter Gap Percent:");
        double gapPercent = Double.parseDouble(gapStr);

        Clinic newClinic = new Clinic(name, fee, gapPercent);
        facilitiesManager.addClinic(newClinic); // Use MedicalFacilitiesManager to add clinic
        loadClinics(); // Refresh the table
    }

    private void loadHospitals() {
        hospitalTableModel.setRowCount(0); // Clear existing rows
        for (Hospital hospital : facilitiesManager.getHospitals()) {
            hospitalTableModel.addRow(new Object[]{hospital.getId(), hospital.getName(), hospital.getProbAdmit()});
        }
    }

    private void loadClinics() {
        clinicTableModel.setRowCount(0); // Clear existing rows
        for (Clinic clinic : facilitiesManager.getClinics()) {
            clinicTableModel.addRow(new Object[]{clinic.getId(), clinic.getName(), clinic.getFee(), clinic.getGapPercent()});
        }
    }

    private void deleteHospital() {
        int selectedRow = hospitalTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) hospitalTableModel.getValueAt(selectedRow, 0);
            facilitiesManager.getHospitals().removeIf(h -> h.getId() == id);
            loadHospitals(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(null, "Please select a hospital to delete.");
        }
    }

    private void deleteClinic() {
        int selectedRow = clinicTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) clinicTableModel.getValueAt(selectedRow, 0);
            facilitiesManager.getClinics().removeIf(c -> c.getId() == id);
            loadClinics(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(null, "Please select a clinic to delete.");
        }
    }

    private void editHospital() {
        int selectedRow = hospitalTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) hospitalTableModel.getValueAt(selectedRow, 0);
            Hospital hospital = facilitiesManager.getHospitals().stream().filter(h -> h.getId() == id).findFirst().orElse(null);
            if (hospital != null) {
                String newName = JOptionPane.showInputDialog("Enter new Hospital Name:", hospital.getName());
                String newProbStr = JOptionPane.showInputDialog("Enter new Admission Probability (0-1):", hospital.getProbAdmit());
                double newProbAdmit = Double.parseDouble(newProbStr);
                hospital.setName(newName);
                hospital.setProbAdmit(newProbAdmit);
                loadHospitals(); // Refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a hospital to edit.");
        }
    }

    private void editClinic() {
        int selectedRow = clinicTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) clinicTableModel.getValueAt(selectedRow, 0);
            Clinic clinic = facilitiesManager.getClinics().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
            if (clinic != null) {
                String newName = JOptionPane.showInputDialog("Enter new Clinic Name:", clinic.getName());
                String newFeeStr = JOptionPane.showInputDialog("Enter new Consultation Fee:", clinic.getFee());
                double newFee = Double.parseDouble(newFeeStr);
                String newGapStr = JOptionPane.showInputDialog("Enter new Gap Percent:", clinic.getGapPercent());
                double newGapPercent = Double.parseDouble(newGapStr);
                clinic.setName(newName);
                clinic.setFee(newFee);
                clinic.setGapPercent(newGapPercent);
                loadClinics(); // Refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a clinic to edit.");
        }
    }

}
