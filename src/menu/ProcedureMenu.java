package menu;

import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import main.MedicalGUI;
import manager.MedicalFacilitiesManager;
import manager.ProcedureManager;
import object.Procedure;
import object.Hospital;

public class ProcedureMenu extends BaseMenu {

    private JTable procedureTable;
    private DefaultTableModel procedureTableModel;
    private ProcedureManager procedureManager; // Use ProcedureManager      
    private JComboBox<Hospital> hospitalComboBox; // Add hospital combo box      
    private MedicalFacilitiesManager facilitiesManager; // Add facilities manager      
    private JComboBox<String> sortComboBox; // Add sort combo box      

    public ProcedureMenu(MedicalGUI app) {
        super(app);
        procedureManager = new ProcedureManager(); // Initialize the ProcedureManager      
        facilitiesManager = new MedicalFacilitiesManager(); // Initialize the MedicalFacilitiesManager      
    }

    public void display() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment      
        panel.setBackground(Color.LIGHT_GRAY);

        // Create components      
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Procedure", createProcedurePanel());

        panel.add(tabbedPane);
        clearAndSetupPanel(panel);
    }

    private JPanel createProcedurePanel() {
        JPanel procedurePanel = new JPanel();
        GroupLayout layout = new GroupLayout(procedurePanel);
        procedurePanel.setLayout(layout);
        procedurePanel.setBackground(Color.LIGHT_GRAY);

        JLabel hospitalLabel = new JLabel("Select Hospital:");
        hospitalComboBox = new JComboBox<>(facilitiesManager.getHospitals().toArray(new Hospital[0])); // Load hospitals      
        hospitalComboBox.addActionListener(e -> loadProceduresForSelectedHospital());

        JLabel sortLabel = new JLabel("Sorted by:");
        sortComboBox = new JComboBox<>(new String[]{"Name (A-Z)", "Name (Z-A)", "ID (Ascending)", "ID (Descending)"});
        sortComboBox.addActionListener(e -> sortProcedures()); // Add sorting action listener      

        procedureTableModel = new DefaultTableModel(new Object[]{"ID", "Procedure Name", "Description", "Type", "Base Cost"}, 0);
        procedureTable = new JTable(procedureTableModel);
        JScrollPane scrollPane = new JScrollPane(procedureTable);

        // Load procedures into the table      
        loadProcedures();

        JButton addButton = new JButton("Add Procedure");
        addButton.addActionListener(e -> addProcedure());
        JButton deleteButton = new JButton("Delete Procedure");
        deleteButton.addActionListener(e -> deleteProcedure());
        JButton editButton = new JButton("Edit Procedure");
        editButton.addActionListener(e -> editProcedure());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> new ObjectManagementMenu(app).display());

        // Set GroupLayout properties      
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(hospitalLabel)
                                .addComponent(hospitalComboBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addGap(30)
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
                                .addComponent(hospitalLabel)
                                .addComponent(hospitalComboBox)
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

        return procedurePanel;
    }

    private void loadProceduresForSelectedHospital() {
        Hospital selectedHospital = (Hospital) hospitalComboBox.getSelectedItem();
        if (selectedHospital != null) {
            procedureTableModel.setRowCount(0); // Clear existing rows      
            List<Procedure> procedures = procedureManager.getProceduresByHospitalId(selectedHospital.getId());
            for (Procedure procedure : procedures) {
                procedureTableModel.addRow(new Object[]{
                    procedure.getId(),
                    procedure.getName(),
                    procedure.getDescription(),
                    procedure.getIsElective() ? "Elective" : "Non-Elective",
                    procedure.getCost()
                });
            }
            sortProcedures(); // Sort procedures after loading      
        }
    }

    private void loadProcedures() {
        // Initially load procedures for the first hospital if needed      
        if (hospitalComboBox.getItemCount() > 0) {
            hospitalComboBox.setSelectedIndex(0); // Select the first hospital      
            loadProceduresForSelectedHospital(); // Load procedures for the selected hospital      
        }
    }

    private void sortProcedures() {
        List<Procedure> procedures = new ArrayList<>();

        // Populate the procedures list from the table model  
        for (int i = 0; i < procedureTableModel.getRowCount(); i++) {
            int id = (int) procedureTableModel.getValueAt(i, 0);
            String name = (String) procedureTableModel.getValueAt(i, 1);
            String description = (String) procedureTableModel.getValueAt(i, 2); // Corrected index  
            boolean isElective = procedureTableModel.getValueAt(i, 3).equals("Elective"); // Corrected index  
            double cost = (double) procedureTableModel.getValueAt(i, 4);
            int hospitalId = 0;

            // Use the constructor that includes the hospitalId  
            procedures.add(new Procedure(id, name, description, isElective, cost, hospitalId));
        }

        // Determine the selected sorting option  
        String selectedSort = (String) sortComboBox.getSelectedItem();
        switch (selectedSort) {
            case "Name (A-Z)":
                procedures.sort(Comparator.comparing(Procedure::getName));
                break;
            case "Name (Z-A)":
                procedures.sort(Comparator.comparing(Procedure::getName).reversed());
                break;
            case "ID (Ascending)":
                procedures.sort(Comparator.comparingInt(Procedure::getId));
                break;
            case "ID (Descending)":
                procedures.sort(Comparator.comparingInt(Procedure::getId).reversed());
                break;
            default:
                // Handle unexpected cases if necessary  
                break;
        }

        // Clear the table and repopulate with sorted procedures  
        procedureTableModel.setRowCount(0);
        for (Procedure procedure : procedures) {
            procedureTableModel.addRow(new Object[]{
                procedure.getId(),
                procedure.getName(),
                procedure.getDescription(),
                procedure.getIsElective() ? "Elective" : "Non-Elective",
                procedure.getCost()
            });
        }
    }

    private void addProcedure() {
        Hospital selectedHospital = (Hospital) hospitalComboBox.getSelectedItem();
        if (selectedHospital == null) {
            JOptionPane.showMessageDialog(null, "Please select a hospital first.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter Procedure Name:");
        String description = JOptionPane.showInputDialog("Enter Procedure Description:");

        // Options for procedure type  
        String[] options = {"Elective", "Non-Elective"};
        String type = (String) JOptionPane.showInputDialog(null, "Select Procedure Type:",
                "Procedure Type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Check if the user canceled the dialog  
        if (type == null) {
            return; // User canceled the dialog  
        }

        boolean isElective = type.equals("Elective"); // Determine if elective is selected  
        String costStr = JOptionPane.showInputDialog("Enter Base Cost:");

        try {
            double cost = Double.parseDouble(costStr);
            Procedure newProcedure = new Procedure(name, description, isElective, cost, selectedHospital.getId());
            procedureManager.addProcedure(newProcedure); // Add to manager        
            procedureTableModel.addRow(new Object[]{
                newProcedure.getId(), // ID is now set by ProcedureManager      
                name,
                description,
                isElective ? "Elective" : "Non-Elective",
                cost
            });
            sortProcedures(); // Sort after adding        
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid cost: " + ex.getMessage());
        }
    }

    private void deleteProcedure() {
        int selectedRow = procedureTable.getSelectedRow();
        if (selectedRow != -1) {
            int procedureId = (int) procedureTableModel.getValueAt(selectedRow, 0); // Get the ID of the selected procedure      
            procedureManager.deleteProcedure(procedureId); // Remove from ProcedureManager      
            procedureTableModel.removeRow(selectedRow); // Remove from table model      
        } else {
            JOptionPane.showMessageDialog(null, "Please select a procedure to delete.");
        }
    }

    private void editProcedure() {
        int selectedRow = procedureTable.getSelectedRow();
        if (selectedRow != -1) {
            int procedureId = (int) procedureTableModel.getValueAt(selectedRow, 0); // Get the ID of the selected procedure      
            Procedure selectedProcedure = procedureManager.getProcedures().stream()
                    .filter(proc -> proc.getId() == procedureId)
                    .findFirst()
                    .orElse(null);

            if (selectedProcedure != null) {
                String newName = JOptionPane.showInputDialog("Enter new Procedure Name:", selectedProcedure.getName());
                String newDescription = JOptionPane.showInputDialog("Enter new Description:", selectedProcedure.getDescription());
                String newType = JOptionPane.showInputDialog("Enter new Procedure Type (Elective/Non-Elective):", selectedProcedure.getIsElective() ? "Elective" : "Non-Elective");
                boolean newIsElective = newType.equalsIgnoreCase("Elective");
                String newCostStr = JOptionPane.showInputDialog("Enter new Base Cost:", selectedProcedure.getCost());

                try {
                    double newCost = Double.parseDouble(newCostStr);
                    selectedProcedure.setName(newName);
                    selectedProcedure.setDescription(newDescription);
                    selectedProcedure.setIsElective(newIsElective);
                    selectedProcedure.setCost(newCost);

                    // Update the table model      
                    procedureTableModel.setValueAt(newName, selectedRow, 1);
                    procedureTableModel.setValueAt(newIsElective ? "Elective" : "Non-Elective", selectedRow, 2);
                    procedureTableModel.setValueAt(newDescription, selectedRow, 3);
                    procedureTableModel.setValueAt(newCost, selectedRow, 4);
                    sortProcedures(); // Sort after editing      
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input for cost: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a procedure to edit.");
        }
    }
}
