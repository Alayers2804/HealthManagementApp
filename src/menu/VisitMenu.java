package menu;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import main.HelpHealthManagementApp;
import manager.MedicalFacilitiesManager;
import manager.PatientsManager;
import manager.ProcedureManager;
import object.Patient;
import object.Hospital;
import object.Clinic;
import object.Procedure;

public class VisitMenu extends BaseMenu {

    private JComboBox<Patient> patientComboBox;
    private JComboBox<Hospital> hospitalComboBox;
    private JComboBox<Clinic> clinicComboBox;
    private JTextField idField;
    private JTextField nameField;
    private JTextField statusField;
    private JTextField balanceField;
    private JTextField currentFacilityField;

    private JLabel facilityNameLabel;
    private JLabel admissionProbabilityLabel;
    private JLabel availableProceduresLabel;
    private JTextField facilityIdField;
    private JTextField facilityNameField;
    private JTextField admissionProbabilityField;
    private JTextField availableProceduresField;

    private PatientsManager patientManager;
    private MedicalFacilitiesManager facilitiesManager;
    private ProcedureManager procedureManager;

    public VisitMenu(HelpHealthManagementApp app) {
        super(app);
        facilitiesManager = new MedicalFacilitiesManager();
        patientManager = new PatientsManager();
        procedureManager = new ProcedureManager();

    }

    public void display() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // Use BorderLayout for the main panel
        panel.setBackground(Color.LIGHT_GRAY);

        // Patient Information Panel
        JPanel patientPanel = new JPanel();
        patientPanel.setLayout(new BoxLayout(patientPanel, BoxLayout.Y_AXIS));
        patientPanel.setBackground(Color.LIGHT_GRAY);

        JLabel patientLabel = new JLabel("Select Patient");
        patientComboBox = new JComboBox<>(patientManager.getPatients().toArray(new Patient[0]));
        patientComboBox.addActionListener(e -> updatePatientDetails());

        JLabel selectedPatientLabel = new JLabel("Selected Patient Details");
        idField = new JTextField();
        nameField = new JTextField();
        statusField = new JTextField();
        balanceField = new JTextField();
        currentFacilityField = new JTextField();

        // Disable text fields for patient details
        idField.setEditable(false);
        nameField.setEditable(false);
        statusField.setEditable(false);
        balanceField.setEditable(false);
        currentFacilityField.setEditable(false);

        patientPanel.add(patientLabel);
        patientPanel.add(patientComboBox);
        patientPanel.add(selectedPatientLabel);
        patientPanel.add(new JLabel("ID:"));
        patientPanel.add(idField);
        patientPanel.add(new JLabel("Patient Name:"));
        patientPanel.add(nameField);
        patientPanel.add(new JLabel("Status:"));
        patientPanel.add(statusField);
        patientPanel.add(new JLabel("Balance:"));
        patientPanel.add(balanceField);
        patientPanel.add(new JLabel("Current Facility:"));
        patientPanel.add(currentFacilityField);

        // Facility Information Panel
        JPanel facilityPanel = new JPanel();
        facilityPanel.setLayout(new BoxLayout(facilityPanel, BoxLayout.Y_AXIS));
        facilityPanel.setBackground(Color.LIGHT_GRAY);

        JLabel facilityLabel = new JLabel("Select Facility");
        JRadioButton hospitalButton = new JRadioButton("Hospital");
        JRadioButton clinicButton = new JRadioButton("Clinic");
        ButtonGroup facilityGroup = new ButtonGroup();
        facilityGroup.add(hospitalButton);
        facilityGroup.add(clinicButton);

        hospitalComboBox = new JComboBox<>(facilitiesManager.getHospitals().toArray(new Hospital[0]));
        clinicComboBox = new JComboBox<>(facilitiesManager.getClinics().toArray(new Clinic[0]));

        hospitalComboBox.setVisible(false);
        clinicComboBox.setVisible(false);

        hospitalButton.addActionListener(e -> {
            hospitalComboBox.setVisible(true);
            clinicComboBox.setVisible(false);
            updateFacilityDetails(); // Update details when hospital button is selected
        });

        clinicButton.addActionListener(e -> {
            clinicComboBox.setVisible(true);
            hospitalComboBox.setVisible(false);
            updateFacilityDetails(); // Update details when clinic button is selected
        });

        // Add action listeners to combo boxes to update details when selection changes
        hospitalComboBox.addActionListener(e -> updateFacilityDetails());
        clinicComboBox.addActionListener(e -> updateFacilityDetails());

        // Initialize labels and fields
        facilityNameLabel = new JLabel();
        admissionProbabilityLabel = new JLabel();
        availableProceduresLabel = new JLabel();
        facilityIdField = new JTextField();
        facilityNameField = new JTextField();
        admissionProbabilityField = new JTextField();
        availableProceduresField = new JTextField();

        // Disable text fields for facility details
        facilityIdField.setEditable(false);
        facilityNameField.setEditable(false);
        admissionProbabilityField.setEditable(false);
        availableProceduresField.setEditable(false);

        facilityPanel.add(facilityLabel);
        facilityPanel.add(hospitalButton);
        facilityPanel.add(clinicButton);
        facilityPanel.add(hospitalComboBox);
        facilityPanel.add(clinicComboBox);
        facilityPanel.add(new JLabel("ID:"));
        facilityPanel.add(facilityIdField);
        facilityPanel.add(facilityNameLabel);
        facilityPanel.add(facilityNameField);
        facilityPanel.add(admissionProbabilityLabel);
        facilityPanel.add(admissionProbabilityField);
        facilityPanel.add(availableProceduresLabel);
        facilityPanel.add(availableProceduresField);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Use FlowLayout for buttons
        JButton processVisitButton = new JButton("Process Visit");
        processVisitButton.addActionListener(e -> processVisit());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> new TransactionMenu(app).display());

        buttonPanel.add(processVisitButton);
        buttonPanel.add(backButton);

        // Add panels to the main panel
        panel.add(patientPanel, BorderLayout.WEST);
        panel.add(facilityPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH); // Place buttons at the bottom

        clearAndSetupPanel(panel);
    }

    private void updatePatientDetails() {
        Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
        if (selectedPatient != null) {
            idField.setText(String.valueOf(selectedPatient.getId()));
            nameField.setText(selectedPatient.getName());
            statusField.setText(selectedPatient.isPrivate() ? "Private" : "Public");
            balanceField.setText(String.valueOf(selectedPatient.getBalance()));
            currentFacilityField.setText(selectedPatient.getCurrentFacility() != null ? selectedPatient.getCurrentFacility().getName() : "None");
        }
    }

    private void updateFacilityDetails() {
        if (hospitalComboBox.isVisible()) {
            facilityNameLabel.setText("Hospital Name:");
            admissionProbabilityLabel.setText("Admission Probability:");
            availableProceduresLabel.setText("Available Procedures:");

            Hospital selectedHospital = (Hospital) hospitalComboBox.getSelectedItem();
            if (selectedHospital != null) {
                // Use MedicalFacilitiesManager to retrieve hospital details
                facilityIdField.setText(String.valueOf(selectedHospital.getId()));
                facilityNameField.setText(selectedHospital.getName());
                admissionProbabilityField.setText(String.valueOf(selectedHospital.getProbAdmit()));

                // Retrieve procedures for the selected hospital
                List<Procedure> procedures = facilitiesManager.getProcedureManager().getProceduresByHospitalId(selectedHospital.getId());
                // Create a string to display procedure names
                StringBuilder procedureNames = new StringBuilder();
                for (Procedure procedure : procedures) {
                    procedureNames.append(procedure.getName()).append(", ");
                }
                // Remove the last comma and space if there are any procedures
                if (procedureNames.length() > 0) {
                    procedureNames.setLength(procedureNames.length() - 2); // Remove last comma and space
                }
                availableProceduresField.setText(procedureNames.toString() + " (Count: " + procedures.size() + ")"); // Display names and count
            }
        } else if (clinicComboBox.isVisible()) {
            facilityNameLabel.setText("Clinic Name:");
            admissionProbabilityLabel.setText("Fee:");
            availableProceduresLabel.setText("Gap Percent:");

            Clinic selectedClinic = (Clinic) clinicComboBox.getSelectedItem();
            if (selectedClinic != null) {
                // Use MedicalFacilitiesManager to retrieve clinic details
                facilityIdField.setText(String.valueOf(selectedClinic.getId()));
                facilityNameField.setText(selectedClinic.getName());
                admissionProbabilityField.setText(String.valueOf(selectedClinic.getFee()));
                availableProceduresField.setText(String.valueOf(selectedClinic.getGapPercent()));
            }
        }
    }

    private void processVisit() {
        // Logic to process the visit
        // This could involve updating the patient's current facility, etc.
    }

    private void refreshData() {
        patientComboBox.setModel(new DefaultComboBoxModel<>(patientManager.getPatients().toArray(new Patient[0])));
        hospitalComboBox.setModel(new DefaultComboBoxModel<>(facilitiesManager.getHospitals().toArray(new Hospital[0])));
        clinicComboBox.setModel(new DefaultComboBoxModel<>(facilitiesManager.getClinics().toArray(new Clinic[0])));
    }
}