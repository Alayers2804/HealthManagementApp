package object;

public class Patient {
    
    public int id; // Patient ID
    private String name; // Patient name
    private boolean isPrivate; // Private or public patient
    private double balance; // Account balance
    private MedicalFacility currentFacility; // Current medical facility

    public Patient(String name, boolean isPrivate, double balance) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.balance = balance;
        this.currentFacility = null; // Default to no facility assigned
    }

    // Getter for patient ID
    public int getId() {
        return id;
    }

    // Setter for patient ID (used by manager during loading)
    public void setId(int id) {
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for private status
    public boolean isPrivate() {
        return isPrivate;
    }

    // Getter for balance
    public double getBalance() {
        return balance;
    }

    public void addBalance(double balance) {
        this.balance = balance;
    }

    // Getter for current facility
    public MedicalFacility getCurrentFacility() {
        return currentFacility;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter for current facility
    public void setCurrentFacility(MedicalFacility currentFacility) {
        this.currentFacility = currentFacility;
    }

    // Method to retrieve facility ID as a string
    public String getFacilityId() {
        if (currentFacility != null) {
            return String.valueOf(currentFacility.getId()); // Assuming getId() exists
        }
        return null;
    }

    public void setStatus(boolean status) {
        this.isPrivate = status; // Update the isPrivate field
    }

    @Override
    public String toString() {
        return "Patient{id: " + id + ", name: " + name
                + ", balance: " + balance
                + ", currentFacility: " + (currentFacility != null ? currentFacility.getName() : "None") + "}";
    }
}
