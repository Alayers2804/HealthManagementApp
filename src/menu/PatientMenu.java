package menu;

import manager.PatientsManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import main.HelpHealthManagementApp;
import manager.MedicalFacilitiesManager;
import object.Patient;

public class PatientMenu extends BaseMenu {

    private JTable patientTable;
    private DefaultTableModel patientTableModel;
    private PatientsManager patientManager; // Use PatientManager
    private MedicalFacilitiesManager facilitiesManager;

    public PatientMenu(HelpHealthManagementApp app) {
        super(app);
        patientManager = new PatientsManager(); // Initialize the PatientManager
    }

    public void display() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment
        panel.setBackground(Color.LIGHT_GRAY);

        // Create components
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Patient", createPatientPanel());

        panel.add(tabbedPane);
        clearAndSetupPanel(panel);
    }

    private JPanel createPatientPanel() {
        JPanel patientPanel = new JPanel();
        GroupLayout layout = new GroupLayout(patientPanel);
        patientPanel.setLayout(layout);
        patientPanel.setBackground(Color.LIGHT_GRAY);

        JLabel sortLabel = new JLabel("Sorted by:");
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Name (A-Z)", "Name (Z-A)", "ID (Ascending)", "ID (Descending)"});

        JLabel idLabel = new JLabel("Enter ID:");
        JTextField idTextField = new JTextField();

        patientTableModel = new DefaultTableModel(new Object[]{"ID", "Patient Name", "Status", "Balance", "Current Facility"}, 0);
        patientTable = new JTable(patientTableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(e -> addPatient());
        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.addActionListener(e -> deletePatient());
        JButton editButton = new JButton("Edit Patient");
        editButton.addActionListener(e -> editPatient());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> new ObjectManagementMenu(app).display());

        // Load patients into the table
        loadPatients();

        // Set GroupLayout properties
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(sortLabel)
                                .addComponent(sortComboBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addGap(30)
                                .addComponent(idLabel)
                                .addComponent(idTextField, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(idLabel)
                                .addComponent(idTextField)
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

        return patientPanel;
    }

    private void loadPatients() {
        for (Patient patient : patientManager.getPatients()) {
            patientTableModel.addRow(new Object[]{patient.getId(), patient.getName(), patient.isPrivate() ? "Private" : "Public", patient.getBalance(), patient.getCurrentFacility() != null ? patient.getCurrentFacility().getName() : "None"});
        }
    }

    private void addPatient() {
        String name = JOptionPane.showInputDialog("Enter Patient Name:");
        String status = JOptionPane.showInputDialog("Enter Patient Status (Private/Public):");
        boolean isPrivate = status.equalsIgnoreCase("Private");

        // Create a new patient and set its hospital to null explicitly
        Patient newPatient = new Patient(name, isPrivate);
        newPatient.setId(patientManager.getNextPatientId());  // Set the correct ID

        patientManager.addPatient(newPatient);  // Add the patient using PatientManager
        patientTableModel.addRow(new Object[]{
            newPatient.getId(),
            name,
            status,
            newPatient.getBalance(),
            "None"
        });
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            patientManager.getPatients().remove(selectedRow); // Remove from PatientManager
            patientTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a patient to delete.");
        }
    }

    private void editPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            Patient selectedPatient = patientManager.getPatients().get(selectedRow);

            String newName = JOptionPane.showInputDialog("Enter new Patient Name:", selectedPatient.getName());
            String newStatus = JOptionPane.showInputDialog("Enter new Status (Private/Public):", selectedPatient.isPrivate() ? "Private" : "Public");
            boolean newIsPrivate = newStatus.equalsIgnoreCase("Private");

            selectedPatient.setName(newName);
            selectedPatient.setStatus(newIsPrivate ? "Private" : "Public");

            patientTableModel.setValueAt(newName, selectedRow, 1);
            patientTableModel.setValueAt(newStatus, selectedRow, 2);
            patientTableModel.setValueAt(selectedPatient.getBalance(), selectedRow, 3);
            patientTableModel.setValueAt(selectedPatient.getCurrentFacility() != null ? selectedPatient.getCurrentFacility().getName() : "None", selectedRow, 4);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a patient to edit.");
        }
    }
}
