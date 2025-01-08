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
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import main.MedicalGUI;
import object.Patient;
import object.Hospital;
import object.Procedure;

public class OperateMenu extends BaseMenu {

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

    public OperateMenu(MedicalGUI app) {
        super(app);
        facilitiesManager = new MedicalFacilitiesManager();
        patientManager = new PatientsManager();
        procedureManager = new ProcedureManager();
    }

    public void display() {
        JPanel panel = new JPanel();
        panel.setLayout(new CardLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Step 1: Select Facility (Hospital)    
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

        // Initially load all facilities (hospitals)    
        loadFacilities();
    }

    private JPanel createSelectFacilityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel("Select Medical Facility");

        facilitiesTable = new JTable();
        facilitiesTable.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Id", "Facility Name", "Type"}));
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
                new String[]{"Id", "Patient Name", "Status", "Balance"})); // Added "Id" column  
        patientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    int patientId = patientManager.getPatients().get(selectedRow).getId(); // Get the patient ID    
                    updatePatientDetails(patientId); // Pass the patient ID to the updated method    

                    CardLayout cl = (CardLayout) panel.getParent().getLayout();
                    cl.show(panel.getParent(), "Select Procedure");
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
                new String[]{"Id", "Procedure Name", "Cost"})); // Added "Id" column  
        JButton submitButton = new JButton("Submit");

        // When submit button is clicked, process the visit and show the next screen    
        submitButton.addActionListener(e -> {
            int selectedPatientRow = patientTable.getSelectedRow();
            int selectedProcedureRow = procedureTable.getSelectedRow();

            if (selectedPatientRow != -1 && selectedProcedureRow != -1) {
                
                int patientId = (int) patientTable.getValueAt(selectedPatientRow, 0);

                int procedureId = (int) procedureTable.getValueAt(selectedProcedureRow, 0); // Get the procedure ID    

                processVisit(patientId, procedureId);
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

    private void updatePatientDetails(int patientId) {
        Patient selectedPatient = patientManager.getPatients().stream()
                .filter(patient -> patient.getId() == patientId)
                .findFirst()
                .orElse(null);

        if (selectedPatient != null) {
            patientNameField.setText(selectedPatient.getName());
            patientStatusField.setText(selectedPatient.isPrivate() ? "Private" : "Public");
        } else {
            // Optionally handle the case where the patient is not found    
            JOptionPane.showMessageDialog(null, "Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processVisit(int patientId, int procedureId) {
        // Retrieve the selected patient, procedure, and facility using the provided IDs      
        Patient selectedPatient = patientManager.getPatients().stream()
                .filter(patient -> patient.getId() == patientId)
                .findFirst()
                .orElse(null);

        Procedure selectedProcedure = procedureManager.getProcedures().stream()
                .filter(procedure -> procedure.getId() == procedureId)
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

        // Calculate the cost of the procedure    
        double cost = selectedProcedure.calculateCost(selectedPatient);

        // Update the patient's balance    
        double newBalance = selectedPatient.getBalance() - cost; // Deduct the cost from the balance    
        selectedPatient.addBalance(newBalance); // Ensure you have a method to set the balance    

        // Optionally set the current facility if needed    
        selectedPatient.setCurrentFacility(null); // Set the current facility to the selected hospital    

        // Update the patient in the PatientsManager    
        patientManager.updatePatient(selectedPatient); // Save the updated patient data      

        // Update UI fields      
        costField.setText(String.valueOf(cost));
        procedureTypeField.setText(selectedProcedure.getIsElective() ? "Elective" : "Non-Elective"); // Set the procedure name in the UI  
        newBalanceField.setText(String.valueOf(selectedPatient.getBalance()));

        // Show the operation complete message      
        JOptionPane.showMessageDialog(null, "Operation Complete", "OPERATION COMPLETE", JOptionPane.INFORMATION_MESSAGE);
    }

    private List<Patient> loadPatientsForSelectedFacility(int facilityRow) {
        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
        model.setRowCount(0); // Clear existing rows    

        if (facilityRow != -1) {
            int facilityId = (int) facilitiesTable.getValueAt(facilityRow, 0);

            List<Patient> patients = loadPatientsForHospital(facilityId, model);
            loadProceduresForSelectedHospital(facilityId, patients); // Load procedures for the selected hospital    
            return patients;

        }
        return List.of(); // Return an empty list if no facility is selected or invalid row    
    }

    private List<Patient> loadPatientsForHospital(int facilityId, DefaultTableModel model) {
        Hospital selectedHospital = facilitiesManager.getHospitals().stream()
                .filter(hospital -> hospital.getId() == facilityId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found for ID: " + facilityId));

        System.out.println("Selected Hospital: " + selectedHospital.getName() + ", ID: " + selectedHospital.getId());

        List<Patient> patients = patientManager.getPatientsByHospital(selectedHospital.getId());
        System.out.println("Patients for Hospital: " + patients);

        addPatientsToTable(patients, model);
        return patients;
    }

    private void addPatientsToTable(List<Patient> patients, DefaultTableModel model) {
        for (Patient patient : patients) {
            model.addRow(new Object[]{patient.getId(), patient.getName(), patient.isPrivate() ? "Private" : "Public", patient.getBalance()}); // Added ID  
        }
    }

    private void loadProceduresForSelectedHospital(int facilityId, List<Patient> patients) {
        // Validate inputs    
        if (patients == null || patients.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Patients need to be assigned first.", "Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        DefaultTableModel model = (DefaultTableModel) procedureTable.getModel();
        model.setRowCount(0); // Clear existing rows in the table    

        // Fetch procedures for the selected hospital    
        List<Procedure> procedures = procedureManager.getProceduresByHospitalId(facilityId);
        if (procedures == null || procedures.isEmpty()) {
            System.out.println("No procedures found for hospital ID: " + facilityId);
            return;
        }

        for (Procedure procedure : procedures) {
            double totalCost = 0.0;

            // Calculate the total cost dynamically for all patients    
            for (Patient patient : patients) {
                totalCost += procedure.calculateCost(patient); // Ensure calculateCost is implemented correctly    
            }

            model.addRow(new Object[]{procedure.getId(), procedure.getName(), totalCost}); // Added ID  
        }
    }

    private void loadFacilities() {
        DefaultTableModel model = (DefaultTableModel) facilitiesTable.getModel();
        model.setRowCount(0); // Clear existing rows    

        // Load hospitals    
        List<Hospital> hospitals = facilitiesManager.getHospitals();
        for (Hospital hospital : hospitals) {
            model.addRow(new Object[]{hospital.getId(), hospital.getName(), "Hospital"});
        }
    }
}
