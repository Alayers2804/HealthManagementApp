package menu;

import manager.PatientsManager;
import manager.MedicalFacilitiesManager;
import manager.ProcedureManager;
import javax.swing.table.DefaultTableModel;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.util.Random;
import main.HelpHealthManagementApp;
import object.Patient;
import object.Hospital;
import object.Procedure;
import object.Clinic;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ProcedureOperationMenu extends BaseMenu {

    private JTable facilitiesTable;
    private JTable patientTable;
    private JTable procedureTable;
    private JTextField patientNameField;
    private JTextField procedureTypeField;
    private JTextField patientStatusField;
    private JTextField costField;
    private JTextField newBalanceField;

    private PatientsManager patientManager;
    private MedicalFacilitiesManager facilitiesManager;
    private ProcedureManager procedureManager;

    public ProcedureOperationMenu(HelpHealthManagementApp app) {
        super(app);
        facilitiesManager = new MedicalFacilitiesManager();
        patientManager = new PatientsManager();
        procedureManager = new ProcedureManager();
    }

    public void display() {
        JPanel panel = new JPanel();
        panel.setLayout(new CardLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Step 1: Select Facility (Hospital or Clinic)
        JPanel selectFacilityPanel = createSelectFacilityPanel();
        panel.add(selectFacilityPanel, "Select Facility");

        // Step 2: Select Patient
        JPanel selectPatientPanel = createSelectPatientPanel();
        panel.add(selectPatientPanel, "Select Patient");

        // Step 3: Select Procedure
        JPanel selectProcedurePanel = createSelectProcedurePanel();
        panel.add(selectProcedurePanel, "Select Procedure");

        // Step 4: Operation Complete
        JPanel operationCompletePanel = createOperationCompletePanel();
        panel.add(operationCompletePanel, "Operation Complete");

        // Show the first panel
        CardLayout cl = (CardLayout) (panel.getLayout());
        cl.show(panel, "Select Facility");

        // Add the main panel to the frame
        clearAndSetupPanel(panel);

        // Initially load all facilities (hospitals and clinics)
        loadFacilities();
    }

    private JPanel createSelectFacilityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel("Select Medical Facility");

        facilitiesTable = new JTable();
        facilitiesTable.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Facility Name", "Type"}));
        facilitiesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = facilitiesTable.getSelectedRow();
                System.out.println("Selected Row: " + row);  // Debugging log

                if (row != -1) {

                    loadPatientsForSelectedFacility(row);

                    // Move to the next panel
                    CardLayout cl = (CardLayout) panel.getParent().getLayout();
                    cl.show(panel.getParent(), "Select Patient");
                } else {
                    System.out.println("Invalid row selection.");
                }
            }
        });

        JButton nextButton = new JButton("Next");
        panel.add(label);
        panel.add(new JScrollPane(facilitiesTable));
        panel.add(nextButton);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new TransactionMenu(app).display(); // Navigate back to Transaction Menu
        });
        panel.add(backButton);

        return panel;
    }

    private JPanel createSelectPatientPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel("Select Patient");

        patientTable = new JTable();
        patientTable.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Patient Name", "Status", "Balance"}));
        patientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    updatePatientDetails(); // Update patient details

                    // Get the selected facility type
                    int selectedFacilityRow = facilitiesTable.getSelectedRow();
                    if (selectedFacilityRow != -1) {
                        String facilityType = (String) facilitiesTable.getValueAt(selectedFacilityRow, 1);

                        // Check if the selected facility is a Clinic
                        if (facilityType.equals("Clinic")) {
                            processVisitForClinic(); // Directly process visit for clinic
                        } else {
                            // Move to the next panel for Hospital
                            CardLayout cl = (CardLayout) panel.getParent().getLayout();
                            cl.show(panel.getParent(), "Select Procedure");
                        }
                    }
                }
            }
        });

        JButton nextButton = new JButton("Next");
        panel.add(label);
        panel.add(new JScrollPane(patientTable));
        panel.add(nextButton);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) panel.getParent().getLayout();
            cl.show(panel.getParent(), "Select Facility"); // Go back to Select Facility
        });
        panel.add(backButton);

        return panel;
    }

    private JPanel createSelectProcedurePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel("Available Procedures:");

        procedureTable = new JTable();
        procedureTable.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Procedure Name", "Cost"}));
        JButton submitButton = new JButton("Submit");

        // When submit button is clicked, process the visit and show the next screen
        submitButton.addActionListener(e -> {
            int selectedFacilityRow = facilitiesTable.getSelectedRow();
            int selectedPatientRow = patientTable.getSelectedRow();
            int selectedProcedureRow = procedureTable.getSelectedRow();

            if (selectedFacilityRow != -1 && selectedPatientRow != -1 && selectedProcedureRow != -1) {
                // Get the facility name from the selected row
                String facilityName = (String) facilitiesTable.getValueAt(selectedFacilityRow, 0); // Assuming the name is in the first column

                // Retrieve the facility ID using the facility name
                Hospital selectedHospital = facilitiesManager.getHospitals().stream()
                        .filter(hospital -> hospital.getName().equals(facilityName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Hospital not found for Name: " + facilityName));

                int facilityId = selectedHospital.getId(); // Get the ID from the Hospital object

                // Get the patient ID
                int patientId = patientManager.getPatients().get(selectedPatientRow).getId();

                // Get the procedure ID
                int procedureId = procedureManager.getProcedures().get(selectedProcedureRow).getId();

                // Call the processVisit method with the IDs
                processVisit(facilityId, patientId, procedureId);
                CardLayout cl = (CardLayout) panel.getParent().getLayout();
                cl.show(panel.getParent(), "Operation Complete");
            } else {
                JOptionPane.showMessageDialog(null, "Please select a patient and a procedure.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(new JScrollPane(procedureTable));
        panel.add(submitButton);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) panel.getParent().getLayout();
            cl.show(panel.getParent(), "Select Patient"); // Go back to Select Patient
        });
        panel.add(backButton);

        return panel;
    }

    private JPanel createOperationCompletePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        patientNameField = new JTextField();
        procedureTypeField = new JTextField();
        patientStatusField = new JTextField();
        costField = new JTextField();
        newBalanceField = new JTextField();

        // Disable text fields for patient details
        patientNameField.setEditable(false);
        procedureTypeField.setEditable(false);
        patientStatusField.setEditable(false);
        costField.setEditable(false);
        newBalanceField.setEditable(false);

        panel.add(new JLabel("Patient Name:"));
        panel.add(patientNameField);
        panel.add(new JLabel("Procedure Type:"));
        panel.add(procedureTypeField);
        panel.add(new JLabel("Patient Status:"));
        panel.add(patientStatusField);
        panel.add(new JLabel("Cost:"));
        panel.add(costField);
        panel.add(new JLabel("New Balance:"));
        panel.add(newBalanceField);

        JLabel resultLabel = new JLabel("Operation Complete");
        panel.add(resultLabel);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            new TransactionMenu(app).display();
        });
        panel.add(okButton);

        return panel;
    }

    private void updatePatientDetails() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            Patient selectedPatient = patientManager.getPatients().get(selectedRow);
            patientNameField.setText(selectedPatient.getName());
            patientStatusField.setText(selectedPatient.isPrivate() ? "Private" : "Public");
        }
    }

    private void processVisit(int facilityId, int patientId, int procedureId) {
        // Retrieve the selected patient, procedure, and facility using the provided IDs
        Patient selectedPatient = patientManager.getPatients().stream()
                .filter(patient -> patient.getId() == patientId)
                .findFirst()
                .orElse(null);

        Procedure selectedProcedure = procedureManager.getProcedures().stream()
                .filter(procedure -> procedure.getId() == procedureId)
                .findFirst()
                .orElse(null);

        Hospital selectedHospital = facilitiesManager.getHospitals().stream()
                .filter(hospital -> hospital.getId() == facilityId)
                .findFirst()
                .orElse(null);

        // Debugging output
        System.out.println("Selected Patient: " + (selectedPatient != null ? selectedPatient.getName() : "null"));
        System.out.println("Selected Procedure: " + (selectedProcedure != null ? selectedProcedure.getName() : "null"));

        // Ensure selectedProcedure is not null
        if (selectedProcedure == null) {
            JOptionPane.showMessageDialog(null, "Selected procedure is not valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ensure selectedPatient is not null
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "Selected patient is not valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double cost = selectedProcedure.calculateCost(selectedPatient);

        // Update the patient's balance
        selectedPatient.addBalance(-cost); // Deduct the cost from the balance

        // Update the patient in the PatientsManager
        patientManager.updatePatient(selectedPatient); // Save the updated patient data

        // Update UI fields
        costField.setText(String.valueOf(cost));
        newBalanceField.setText(String.valueOf(selectedPatient.getBalance()));

        // Generate a random number and compare it to a probability
        Random random = new Random();
        double randomNumber = random.nextDouble(); // Generates a number between 0.0 and 1.0
        double probability = selectedHospital.getProbAdmit(); // Assuming the hospital has a method to get the probability

        // Show appropriate message based on the comparison
        String message;
        if (randomNumber < probability) {
            message = "Patient has been discharged from the hospital\n"
                    + "Random Number: " + String.format("%.2f", randomNumber) + " < Probability: " + probability;
        } else {
            message = "Patient Remains in the Hospital and May Undergo Additional Procedures\n"
                    + "Random Number: " + String.format("%.2f", randomNumber) + " > Probability: " + probability;
        }

        // Show the operation complete message
        JOptionPane.showMessageDialog(null, message, "OPERATION COMPLETE", JOptionPane.INFORMATION_MESSAGE);
    }

    private List<Patient> loadPatientsForSelectedFacility(int facilityRow) {
        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
        model.setRowCount(0); // Clear existing rows

        if (facilityRow != -1) {
            String facilityType = (String) facilitiesTable.getValueAt(facilityRow, 1);
            String facilityName = (String) facilitiesTable.getValueAt(facilityRow, 0);

            if (facilityType.equals("Hospital")) {
                List<Patient> patients = loadPatientsForHospital(facilityName, model);
                loadProceduresForSelectedHospital(facilityName, patients); // Load procedures for the selected hospital
                return patients;
            } else if (facilityType.equals("Clinic")) {
                List<Patient> patients = loadPatientsForClinic(facilityName, model);
                // Directly process the visit for the clinic
                if (!patients.isEmpty()) {
                    // Assuming the first patient is selected for the summary
                    patientTable.setRowSelectionInterval(0, 0);
                    updatePatientDetails(); // Update patient details for the summary

                }
                return patients;
            }
        }
        return List.of(); // Return an empty list if no facility is selected or invalid row
    }

    private List<Patient> loadPatientsForHospital(String facilityName, DefaultTableModel model) {
        Hospital selectedHospital = facilitiesManager.getHospitals().stream()
                .filter(hospital -> hospital.getName().equals(facilityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found for Name: " + facilityName));

        System.out.println("Selected Hospital: " + selectedHospital.getName() + ", ID: " + selectedHospital.getId());

        List<Patient> patients = patientManager.getPatientsByHospital(selectedHospital.getId());
        System.out.println("Patients for Hospital: " + patients);

        addPatientsToTable(patients, model);
        return patients;
    }

    private List<Patient> loadPatientsForClinic(String facilityName, DefaultTableModel model) {
        Clinic selectedClinic = facilitiesManager.getClinics().stream()
                .filter(clinic -> clinic.getName().equals(facilityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found for Name: " + facilityName));

        System.out.println("Selected Clinic: " + selectedClinic.getName() + ", ID: " + selectedClinic.getId());

        List<Patient> patients = patientManager.getPatientsByClinic(selectedClinic.getId());
        System.out.println("Patients for Clinic: " + patients);

        addPatientsToTable(patients, model);
        return patients;
    }

    private void addPatientsToTable(List<Patient> patients, DefaultTableModel model) {
        for (Patient patient : patients) {
            model.addRow(new Object[]{patient.getName(), patient.isPrivate() ? "Private" : "Public", patient.getBalance()});
        }
    }

    private void loadProceduresForSelectedHospital(String facilityName, List<Patient> patients) {
        // Validate inputs
        if (patients == null || patients.isEmpty()) {
            throw new IllegalArgumentException("Patients list cannot be null or empty");
        }

        DefaultTableModel model = (DefaultTableModel) procedureTable.getModel();
        model.setRowCount(0); // Clear existing rows in the table

        // Get the selected hospital by ID
        Hospital selectedHospital = facilitiesManager.getHospitals().stream()
                .filter(hospital -> hospital.getName().equals(facilityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found for Name: " + facilityName));
        if (selectedHospital == null) {
            System.err.println("No hospital found with name: " + facilityName);
            return; // Exit if the hospital does not exist
        }

        // Fetch procedures for the selected hospital
        List<Procedure> procedures = procedureManager.getProceduresByHospitalId(selectedHospital.getId());
        if (procedures == null || procedures.isEmpty()) {
            System.out.println("No procedures found for hospital ID: " + selectedHospital.getId());
            return;
        }

        // Populate table with procedure costs
        for (Procedure procedure : procedures) {
            double totalCost = 0.0;

            // Calculate the total cost dynamically for all patients
            for (Patient patient : patients) {
                totalCost += procedure.calculateCost(patient); // Ensure calculateCost is implemented correctly
            }

            // Add the procedure name and total cost to the table
            model.addRow(new Object[]{procedure.getName(), totalCost});
        }

    }

    private void loadFacilities() {
        DefaultTableModel model = (DefaultTableModel) facilitiesTable.getModel();
        model.setRowCount(0); // Clear existing rows

        // Load hospitals
        List<Hospital> hospitals = facilitiesManager.getHospitals();
        for (Hospital hospital : hospitals) {
            model.addRow(new Object[]{hospital.getName(), "Hospital"});
        }

        // Load clinics
        List<Clinic> clinics = facilitiesManager.getClinics();
        for (Clinic clinic : clinics) {
            model.addRow(new Object[]{clinic.getName(), "Clinic"});
        }
    }

    private void showConsultationSummary(double consultationFee, Patient patient, boolean isClinic) {
        if (!isClinic) {
            // If it's not a clinic, return early or handle accordingly
            return; // Skip showing the summary for hospitals
        }

        // Create a dialog to show the consultation summary
        JDialog summaryDialog = new JDialog();
        summaryDialog.setTitle("Consultation Complete");
        summaryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        summaryDialog.setSize(300, 300);
        summaryDialog.setLocationRelativeTo(null); // Center the dialog

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.LIGHT_GRAY);

        // Create fields for the summary
        JTextField consultationFeeField = new JTextField(String.valueOf(consultationFee));
        JTextField patientStatusField = new JTextField(patient.isPrivate() ? "Private" : "Public");
        JTextField amountChargedField = new JTextField(String.valueOf(consultationFee));
        JTextField newBalanceField = new JTextField(String.valueOf(patient.getBalance()));

        // Disable text fields for summary
        consultationFeeField.setEditable(false);
        patientStatusField.setEditable(false);
        amountChargedField.setEditable(false);
        newBalanceField.setEditable(false);

        // Add components to the panel
        summaryPanel.add(new JLabel("Consultation Fee:"));
        summaryPanel.add(consultationFeeField);
        summaryPanel.add(new JLabel("Patient Status:"));
        summaryPanel.add(patientStatusField);
        summaryPanel.add(new JLabel("Amount Charged:"));
        summaryPanel.add(amountChargedField);
        summaryPanel.add(new JLabel("New Balance:"));
        summaryPanel.add(newBalanceField);

        // Create the OK button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            summaryDialog.dispose(); // Close the dialog
            new TransactionMenu(app).display(); // Navigate to TransactionMenu
        });

        // Add the OK button to the panel
        summaryPanel.add(okButton);

        // Add the summary panel to the dialog
        summaryDialog.add(summaryPanel);
        summaryDialog.setVisible(true); // Show the dialog
    }

    private void processVisitForClinic() {
        int selectedPatientRow = patientTable.getSelectedRow();
        int selectedFacilityRow = facilitiesTable.getSelectedRow();

        if (selectedPatientRow != -1 && selectedFacilityRow != -1) {
            Patient selectedPatient = patientManager.getPatients().get(selectedPatientRow);

            // Get the selected clinic's name
            String facilityName = (String) facilitiesTable.getValueAt(selectedFacilityRow, 0);

            // Retrieve the consultation fee from the selected clinic
            double consultationFee = getConsultationFeeForClinic(facilityName); // Implement this method to get the fee

            double balance = selectedPatient.getBalance();
            // Update the patient's balance
            selectedPatient.addBalance(balance - consultationFee); // Deduct the cost from the balance

            // Set the current facility to null before updating the patient
            selectedPatient.setCurrentFacility(null);

            // Update the patient in the PatientsManager
            patientManager.updatePatient(selectedPatient); // Save the updated patient data

            // Show consultation summary only for clinics
            showConsultationSummary(consultationFee, selectedPatient, true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a patient.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Method to retrieve the consultation fee for the selected clinic
    private double getConsultationFeeForClinic(String facilityName) {
        Clinic selectedClinic = facilitiesManager.getClinics().stream()
                .filter(clinic -> clinic.getName().equals(facilityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found for Name: " + facilityName));

        return selectedClinic.getFee(); // Replace with actual method to get the fee
    }

}
