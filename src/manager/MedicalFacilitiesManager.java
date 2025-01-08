package manager;

import object.Clinic;
import object.Hospital;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import object.Patient;
import object.Procedure;

public class MedicalFacilitiesManager {

    private List<Hospital> hospitals;
    private List<Clinic> clinics;
    private ProcedureManager procedureManager;
    private PatientsManager patientsManager;

    public MedicalFacilitiesManager() {
        hospitals = new ArrayList<>();
        clinics = new ArrayList<>();
        procedureManager = new ProcedureManager();
        patientsManager = new PatientsManager();
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

    public void deleteHospital(int hospitalId) {
        // Remove associated procedures
        procedureManager.deleteProceduresByHospitalId(hospitalId);

        // Set current facility of patients to null if it is the deleted hospital
        for (Patient patient : patientsManager.getPatients()) {
            if (patient.getCurrentFacility() instanceof Hospital && patient.getCurrentFacility().getId() == hospitalId) {
                patient.setCurrentFacility(null); // Set current facility to null
                patientsManager.updatePatient(patient);
            }
        }

        // Now remove the hospital
        hospitals.removeIf(hospital -> hospital.getId() == hospitalId);
        saveHospitals();
    }

    public void deleteClinic(int clinicId) {

        for (Patient patient : patientsManager.getPatients()) {
            if (patient.getCurrentFacility() instanceof Clinic && patient.getCurrentFacility().getId() == clinicId) {
                patient.setCurrentFacility(null); // Set current facility to null
                patientsManager.updatePatient(patient);
            }
        }

        clinics.removeIf(clinic -> clinic.getId() == clinicId);
        saveClinics();
    }

    public void updateHospital(Hospital updatedHospital) {
        for (int i = 0; i < hospitals.size(); i++) {
            if (hospitals.get(i).getId() == updatedHospital.getId()) {
                hospitals.set(i, updatedHospital); // Update the existing hospital
                saveHospitals(); // Save the updated list
                return;
            }
        }
    }

    public void updateClinic(Clinic updatedClinic) {
        for (int i = 0; i < clinics.size(); i++) {
            if (clinics.get(i).getId() == updatedClinic.getId()) {
                clinics.set(i, updatedClinic); // Update the existing clinic
                saveClinics(); // Save the updated list
                return;
            }
        }
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
            Hospital.setIdCounter(maxId + 1);
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
            Clinic.setIdCounter(maxId + 1);
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

    public ProcedureManager getProcedureManager() {
        return procedureManager;
    }

    public void addProcedureToHospital(int hospitalId, Procedure procedure) {
        Hospital hospital = getHospitalById(hospitalId);
        if (hospital != null) {
            hospital.addProcedure(procedure);
        }
    }

    private Hospital getHospitalById(int hospitalId) {
        for (Hospital hospital : hospitals) {
            if (hospital.getId() == hospitalId) {
                return hospital;
            }
        }
        return null; 
    }

}
