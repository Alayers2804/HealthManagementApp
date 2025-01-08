package object;

public class Procedure {

    private int id;
    private String name;
    private String description;
    private boolean isElective;
    private double cost;
    private int hospitalId; // Associate with a hospital    

    // Constructor when loading from file    
    public Procedure(int id, String name, String description, boolean isElective, double cost, int hospitalId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isElective = isElective;
        this.cost = cost;
        this.hospitalId = hospitalId;
    }

    // Constructor for new procedures    
    public Procedure(String name, String description, boolean isElective, double cost, int hospitalId) {
        this.name = name;
        this.description = description;
        this.isElective = isElective;
        this.cost = cost;
        this.hospitalId = hospitalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id; // Method to set the ID    
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
