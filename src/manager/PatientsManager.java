package manager;

import object.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import object.Clinic;
import object.Hospital;
import object.MedicalFacility;

public class PatientsManager {

    private List<Patient> patients;
    private int nextPatientId = 1; // Starts from 1 or your preferred starting ID
    
    public PatientsManager() {
        patients = new ArrayList<>();
        loadPatients();
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        savePatients();
    }

    public int getNextPatientId() {
        return nextPatientId;
    }

    private void loadPatients() {
        int maxId = 0; // Track the highest ID
        try (BufferedReader reader = new BufferedReader(new FileReader("datafile/patients.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {  // At least 5 fields (ID, Name, Private/Public, Balance, Facility)
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    boolean isPrivate = Boolean.parseBoolean(parts[2]);
                    double balance = Double.parseDouble(parts[3]);
                    String facilityType = parts[4];  // e.g., Hospital, Clinic, or None

                    MedicalFacility facility = null;

                    // If facilityType is not "None", parse the facility details
                    if (!facilityType.equals("None")) {
                        try {
                            int facilityId = Integer.parseInt(parts[5]);
                            String facilityName = parts[6];

                            // Create the appropriate MedicalFacility object based on the type
                            if (facilityType.equals("Hospital")) {
                                double probAdmit = Double.parseDouble(parts[7]);
                                facility = new Hospital(facilityId, facilityName, probAdmit);
                            } else if (facilityType.equals("Clinic")) {
                                double fee = Double.parseDouble(parts[7]);
                                double gapPercent = Double.parseDouble(parts[8]);
                                facility = new Clinic(facilityId, facilityName, fee, gapPercent);
                            }
                        } catch (NumberFormatException e) {
                            // If facilityId is "None", the try block will throw a NumberFormatException.
                            // In that case, we skip parsing facility details and set facility to null
                        }
                    }

                    Patient patient = new Patient(name, isPrivate);
                    patient.setId(id);  // Set the patient ID
                    patient.addBalance(balance);  // Set the balance
                    patient.setCurrentFacility(facility);  // Set the associated facility
                    patients.add(patient);

                    // Update maxId to track the highest used ID
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading patients: " + e.getMessage());
        }

        // Ensure the next patient added gets a unique ID
        nextPatientId = maxId + 1;
    }

    private void savePatients() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("datafile/patients.txt"))) {
            for (Patient patient : patients) {
                String facilityDetails = "";
                if (patient.getCurrentFacility() != null) {
                    MedicalFacility facility = patient.getCurrentFacility();
                    facilityDetails = facility.getClass().getSimpleName() + "," + facility.getId() + "," + facility.getName();

                    if (facility instanceof Hospital) {
                        Hospital hospital = (Hospital) facility;
                        facilityDetails += "," + hospital.getProbAdmit();  // Include the probability of admission
                    } else if (facility instanceof Clinic) {
                        Clinic clinic = (Clinic) facility;
                        facilityDetails += "," + clinic.getFee() + "," + clinic.getGapPercent();
                    }
                } else {
                    facilityDetails = "None,None,None";
                }

                // Write patient details along with the facility information
                writer.write(patient.getId() + "," + patient.getName() + "," + patient.isPrivate() + "," + patient.getBalance() + "," + facilityDetails);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving patients: " + e.getMessage());
        }
    }

    public List<Patient> getPatientsByHospital(int hospitalId) {
        List<Patient> hospitalPatients = new ArrayList<>();
        for (Patient patient : patients) {

            if (patient.getCurrentFacility() instanceof Hospital) {
                Hospital hospital = (Hospital) patient.getCurrentFacility();
                if (hospital.getId() == hospitalId) {
                    hospitalPatients.add(patient);
                }
            }
        }
        return hospitalPatients;
    }

}
