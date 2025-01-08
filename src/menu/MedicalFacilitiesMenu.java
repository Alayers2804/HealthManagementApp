package menu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import main.MedicalGUI;
import menu.ObjectManagementMenu;
import java.util.List;
import object.Hospital;
import object.Clinic;
import manager.MedicalFacilitiesManager; // Import the MedicalFacilitiesManager

public class MedicalFacilitiesMenu extends BaseMenu {

    private JTable hospitalTable;
    private DefaultTableModel hospitalTableModel;
    private MedicalFacilitiesManager facilitiesManager; // Use MedicalFacilitiesManager

    private JTable clinicTable;
    private DefaultTableModel clinicTableModel;

    public MedicalFacilitiesMenu(MedicalGUI app) {
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

        // Add action listener for sorting  
        sortComboBox.addActionListener(e -> sortHospitals(sortComboBox.getSelectedItem().toString()));

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

        // Add action listener for sorting  
        sortComboBox.addActionListener(e -> sortClinics(sortComboBox.getSelectedItem().toString()));

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
            int confirmation = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this hospital? This will also delete associated procedures and set current facilities of patients to null.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Delete associated procedures and set patients' current facility to null
                facilitiesManager.deleteHospital(id); // Use the delete method from MedicalFacilitiesManager
                loadHospitals(); // Refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a hospital to delete.");
        }
    }

    private void deleteClinic() {
        int selectedRow = clinicTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) clinicTableModel.getValueAt(selectedRow, 0);
            int confirmation = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this clinic? This will also delete associated procedures and set current facilities of patients to null.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                facilitiesManager.deleteClinic(id); // Use the delete method from MedicalFacilitiesManager
                loadClinics(); // Refresh the table
            }
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

                try {
                    double newProbAdmit = Double.parseDouble(newProbStr);
                    if (newProbAdmit < 0 || newProbAdmit > 1) {
                        throw new NumberFormatException();
                    }

                    // Update the hospital details
                    hospital.setName(newName);
                    hospital.setProbAdmit(newProbAdmit);

                    // Call the update method in MedicalFacilitiesManager
                    facilitiesManager.updateHospital(hospital); // Assuming you have an updateHospital method in MedicalFacilitiesManager

                    loadHospitals(); // Refresh the table
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for admission probability between 0 and 1.");
                }
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
                String newGapStr = JOptionPane.showInputDialog("Enter new Gap Percent:", clinic.getGapPercent());

                try {
                    double newFee = Double.parseDouble(newFeeStr);
                    double newGapPercent = Double.parseDouble(newGapStr);

                    // Update the clinic details
                    clinic.setName(newName);
                    clinic.setFee(newFee);
                    clinic.setGapPercent(newGapPercent);

                    // Call the update method in MedicalFacilitiesManager
                    facilitiesManager.updateClinic(clinic); // Assuming you have an updateClinic method in MedicalFacilitiesManager

                    loadClinics(); // Refresh the table
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers for fee and gap percent.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a clinic to edit.");
        }
    }

    private void sortHospitals(String criteria) {
        List<Hospital> hospitals = facilitiesManager.getHospitals();
        switch (criteria) {
            case "Name (A-Z)":
                hospitals.sort(Comparator.comparing(Hospital::getName));
                break;
            case "Name (Z-A)":
                hospitals.sort(Comparator.comparing(Hospital::getName).reversed());
                break;
            case "ID (Ascending)":
                hospitals.sort(Comparator.comparingInt(Hospital::getId));
                break;
            case "ID (Descending)":
                hospitals.sort(Comparator.comparingInt(Hospital::getId).reversed());
                break;
        }
        loadHospitals(); // Refresh the table after sorting  
    }

    private void sortClinics(String criteria) {
        List<Clinic> clinics = facilitiesManager.getClinics();
        switch (criteria) {
            case "Name (A-Z)":
                clinics.sort(Comparator.comparing(Clinic::getName));
                break;
            case "Name (Z-A)":
                clinics.sort(Comparator.comparing(Clinic::getName).reversed());
                break;
            case "ID (Ascending)":
                clinics.sort(Comparator.comparingInt(Clinic::getId));
                break;
            case "ID (Descending)":
                clinics.sort(Comparator.comparingInt(Clinic::getId).reversed());
                break;
        }
        loadClinics(); // Refresh the table after sorting  
    }

}
