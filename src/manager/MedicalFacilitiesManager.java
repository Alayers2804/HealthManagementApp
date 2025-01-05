package manager;

import object.Clinic;
import object.Hospital;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalFacilitiesManager {

    private List<Hospital> hospitals;
    private List<Clinic> clinics;
    private ProcedureManager procedureManager; // Add this line

    public MedicalFacilitiesManager() {
        hospitals = new ArrayList<>();
        clinics = new ArrayList<>();
        procedureManager = new ProcedureManager(); // Initialize ProcedureManager
        loadHospitals();
        loadClinics();
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void addHospital(Hospital hospital) {
        hospitals.add(hospital);
        saveHospitals();
       
    }

    public void addClinic(Clinic clinic) {
        clinics.add(clinic);
        saveClinics();
        
    }

    private void loadHospitals() {
        try (BufferedReader reader = new BufferedReader(new FileReader("datafile/hospitals.txt"))) {
            String line;
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double admissionProbability = Double.parseDouble(parts[2]);
                    hospitals.add(new Hospital(id, name, admissionProbability));
                    maxId = Math.max(maxId, id);
                }
            }
            Hospital.setIdCounter(maxId + 1); // Set the counter for the next hospital
        } catch (IOException e) {
            System.err.println("Error loading hospitals: " + e.getMessage());
        }
    }

    private void loadClinics() {
        try (BufferedReader reader = new BufferedReader(new FileReader("datafile/clinics.txt"))) {
            String line;
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double fee = Double.parseDouble(parts[2]);
                    double gapPercent = Double.parseDouble(parts[3]);
                    clinics.add(new Clinic(id, name, fee, gapPercent));
                    maxId = Math.max(maxId, id);
                }
            }
            Clinic.setIdCounter(maxId + 1); // Set the counter for the next clinic
        } catch (IOException e) {
            System.err.println("Error loading clinics: " + e.getMessage());
        }
    }

    private void saveHospitals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("datafile/hospitals.txt"))) {
            for (Hospital hospital : hospitals) {
                writer.write(hospital.getId() + "," + hospital.getName() + "," + hospital.getProbAdmit());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving hospitals: " + e.getMessage());
        }
    }

    private void saveClinics() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("datafile/clinics.txt"))) {
            for (Clinic clinic : clinics) {
                writer.write(clinic.getId() + "," + clinic.getName() + "," + clinic.getFee() + "," + clinic.getGapPercent());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving clinics: " + e.getMessage());
        }
    }

    public ProcedureManager getProcedureManager() { // Add this method
        return procedureManager;
    }
    
}
