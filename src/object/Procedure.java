package object;

public class Procedure {

    static int idCounter = 1;
    private int id;
    private String name;
    private String description;
    private boolean isElective;
    private double cost;
    private int hospitalId; // Associate with a hospital

    // Constructor when loading from file
    public Procedure(String name, String description, boolean isElective, double cost, int hospitalId, int id) {
        this.id = id; // Use the ID from the file
        this.name = name;
        this.description = description;
        this.isElective = isElective;
        this.cost = cost;
        this.hospitalId = hospitalId;

        if (id >= idCounter) {
            idCounter = id + 1;  // Update idCounter only if the loaded ID is higher than the current counter
        }
    }

    // Constructor when adding a new procedure
// Constructor for new procedures
    public Procedure(String name, String description, boolean isElective, double cost, int hospitalId) {
        this.id = idCounter++;
        this.name = name;
        this.description = description;
        this.isElective = isElective;
        this.cost = cost;
        this.hospitalId = hospitalId;
    }

    public static int getIdCounter() {
        return idCounter; // Method to get the current ID counter
    }

    public static void setIdCounter(int idCounter) {
        Procedure.idCounter = idCounter; // Method to set the static counter
    }

    public int getId() {
        return id;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public double calculateCost(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        if (patient.isPrivate()) {
            return isElective ? 2000.0 : 1000.0;
        } else {
            return isElective ? cost : 0.0;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsElective() {
        return isElective;
    }

    public double getCost() {
        return cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setIsElective(boolean isElective) {
        this.isElective = isElective;
    }

    @Override
    public String toString() {
        return "Procedure {id: " + id + ", name: " + name + ", description: " + description
                + ", isElective: " + isElective + ", cost: " + cost + ", hospitalId: " + hospitalId + "}";
    }
}
