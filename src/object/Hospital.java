package object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hospital extends MedicalFacility {

    private static int idCounter = 1; // Static counter for Hospital IDs
    private double probAdmit;
    private List<Procedure> procedures;
    private double lastRandNum;

    // Constructor with ID for loading from files
    public Hospital(int id, String name, double probAdmit) {
        super(id, name);
        this.probAdmit = probAdmit;
        this.procedures = new ArrayList<>();
    }

    // Constructor without ID for new instances
    public Hospital(String name, double probAdmit) {
        super(name);
        this.probAdmit = probAdmit;
        this.procedures = new ArrayList<>();
        this.id = generateId(); // Generate a new ID
    }

    public double getProbAdmit() {
        return probAdmit;
    }

    public void setProbAdmit(double probAdmit) {
        this.probAdmit = probAdmit;
    }

    public List<Procedure> getProcedures() {
        return procedures;
    }

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
    }

    public static int generateId() {
        return idCounter++;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    @Override
    public String toString() {
        return "Hospital {" + super.toString() + ", ProbAdmit: " + probAdmit
                + ", Procedures: " + procedures.size() + "}";
    }
}
