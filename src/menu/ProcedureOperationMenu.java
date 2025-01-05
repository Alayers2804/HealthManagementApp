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
import main.HelpHealthManagementApp;
import object.Patient;
import object.Hospital;
import object.Procedure;

import java.util.List;

public class ProcedureOperationMenu extends BaseMenu {

    private JTable hospitalTable;
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

        // Step 1: Select Hospital
        JPanel selectHospitalPanel = createSelectHospitalPanel();
        panel.add(selectHospitalPanel, "Select Hospital");

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
        cl.show(panel, "Select Hospital");

        // Add the main panel to the frame
        clearAndSetupPanel(panel);

        // Initially load all hospitals
        loadHospitals();
    }

    private JPanel createSelectHospitalPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel("Select Hospital");

        hospitalTable = new JTable();
        hospitalTable.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Hospital Name", "Location"}));
        hospitalTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = hospitalTable.getSelectedRow();
                loadPatientsForSelectedHospital(row);
                loadProceduresForSelectedHospital(); // Call to load procedures after selecting a hospital
                CardLayout cl = (CardLayout) panel.getParent().getLayout();
                cl.show(panel.getParent(), "Select Patient");
            }
        });
        JButton nextButton = new JButton("Next");
        panel.add(label);
        panel.add(new JScrollPane(hospitalTable));
        panel.add(nextButton);

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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updatePatientDetails();
                CardLayout cl = (CardLayout) panel.getParent().getLayout();
                cl.show(panel.getParent(), "Select Procedure");
            }
        });

        JButton nextButton = new JButton("Next");
        panel.add(label);
        panel.add(new JScrollPane(patientTable));
        panel.add(nextButton);

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
            processVisit();
            CardLayout cl = (CardLayout) panel.getParent().getLayout();
            cl.show(panel.getParent(), "Operation Complete");
        });

        panel.add(label);
        panel.add(new JScrollPane(procedureTable));
        panel.add(submitButton);

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

        JLabel resultLabel = new JLabel("Patient has been discharged from the hospital");
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

    private void processVisit() {
        int selectedPatientRow = patientTable.getSelectedRow();
        int selectedProcedureRow = procedureTable.getSelectedRow();

        if (selectedPatientRow != -1 && selectedProcedureRow != -1) {
            Patient selectedPatient = patientManager.getPatients().get(selectedPatientRow);
            Procedure selectedProcedure = procedureManager.getProcedures().get(selectedProcedureRow);

            double cost = selectedProcedure.calculateCost(selectedPatient);

            // Update the patient's balance
            selectedPatient.addBalance(cost);

            costField.setText(String.valueOf(cost));
            newBalanceField.setText(String.valueOf(selectedPatient.getBalance()));
        }
    }

    private void loadHospitals() {
        // Load hospital data into the table
        DefaultTableModel model = (DefaultTableModel) hospitalTable.getModel();
        model.setRowCount(0); // Clear existing rows

        List<Hospital> hospitals = facilitiesManager.getHospitals(); // Explicitly declare the type
        for (Hospital hospital : hospitals) {
            model.addRow(new Object[]{hospital.getName()});
        }
    }

    private void loadPatientsForSelectedHospital(int hospitalRow) {
        // Load patient data for the selected hospital
        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
        model.setRowCount(0); // Clear existing rows

        if (hospitalRow != -1) {
            Hospital selectedHospital = facilitiesManager.getHospitals().get(hospitalRow);
            List<Patient> patients = patientManager.getPatientsByHospital(selectedHospital.getId()); // Explicitly declare the type
            for (Patient patient : patients) {
                model.addRow(new Object[]{patient.getName(), patient.isPrivate() ? "Private" : "Public", patient.getBalance()});
            }
        }
    }

    private void loadProceduresForSelectedHospital() {
        int selectedRow = hospitalTable.getSelectedRow();
        System.out.println("Selected Row Index: " + selectedRow); // Debugging output
        if (selectedRow != -1) {
            // Get the selected hospital
            Hospital selectedHospital = facilitiesManager.getHospitals().get(selectedRow);
            System.out.println("Selected Hospital: " + selectedHospital.getName() + " (ID: " + selectedHospital.getId() + ")");

            // Retrieve procedures for the selected hospital
            List<Procedure> procedures = procedureManager.getProceduresByHospitalId(selectedHospital.getId()); // Explicitly declare the type
            System.out.println("Number of procedures found: " + procedures.size());

            DefaultTableModel model = (DefaultTableModel) procedureTable.getModel();
            model.setRowCount(0); // Clear existing rows

            // Check if procedures are loaded correctly
            if (procedures.isEmpty()) {
                System.out.println("No procedures found for this hospital.");
            } else {
                for (Procedure procedure : procedures) {
                    // Print each procedure's details for debugging
                    System.out.println("Loading Procedure: " + procedure.getName() + ", Cost: " + procedure.calculateCost(null));
                    model.addRow(new Object[]{procedure.getName(), procedure.calculateCost(null)});
                }
            }
        } else {
            System.out.println("No hospital selected.");
        }
    }
}
