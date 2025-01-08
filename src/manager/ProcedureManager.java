package manager;

import object.Procedure;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedureManager {

    private List<Procedure> procedures;
    private int maxId;  // Variable to track the highest ID      

    public ProcedureManager() {
        procedures = new ArrayList<>();
        loadProcedures();
    }

    public List<Procedure> getProcedures() {
        return procedures;
    }

    public void addProcedure(Procedure procedure) {
        // Assign a new ID to the procedure      
        procedure.setId(maxId + 1);
        maxId++; // Increment maxId for the next procedure      
        procedures.add(procedure);
        saveProcedures();
    }

    // New method to delete a procedure by ID      
    public void deleteProcedure(int procedureId) {
        procedures.removeIf(procedure -> procedure.getId() == procedureId);
        saveProcedures(); // Save the updated list after deletion      
    }

    private void loadProcedures() {
        try (BufferedReader reader = new BufferedReader(new FileReader("datafile/procedures.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) { // Ensure there are enough parts      
                    int id = Integer.parseInt(parts[0]);  // Read ID first    
                    String name = parts[1];
                    String description = parts[2];
                    boolean isElective = Boolean.parseBoolean(parts[3]);
                    double cost = Double.parseDouble(parts[4]);
                    int hospitalId = Integer.parseInt(parts[5]);

                    procedures.add(new Procedure(id, name, description, isElective, cost, hospitalId));

                    // Track maxId to avoid unnecessary increments      
                    if (id > maxId) {
                        maxId = id;
                    }
                } else {
                    System.err.println("Invalid procedure data: " + line); // Log invalid lines      
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading procedures: " + e.getMessage());
        }
    }

    private void saveProcedures() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("datafile/procedures.txt"))) {
            for (Procedure procedure : procedures) {
                writer.write(procedure.getId() + "," + procedure.getName() + "," + procedure.getDescription() + ","
                        + procedure.getIsElective() + "," + procedure.getCost() + "," + procedure.getHospitalId());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving procedures: " + e.getMessage());
        }
    }

    public List<Procedure> getProceduresByHospitalId(int hospitalId) {
        List<Procedure> hospitalProcedures = new ArrayList<>();
        for (Procedure procedure : procedures) {
            if (procedure.getHospitalId() == hospitalId) {
                hospitalProcedures.add(procedure);
            }
        }
        return hospitalProcedures;
    }

    public int getMaxId() {
        return maxId;
    }

    public void deleteProceduresByHospitalId(int hospitalId) {
        procedures.removeIf(procedure -> procedure.getHospitalId() == hospitalId);
        saveProcedures(); // Save the updated list      
    }
}
