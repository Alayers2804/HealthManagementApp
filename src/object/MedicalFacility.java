package object;

public abstract class MedicalFacility {

    int id;
    private String name;

    // Constructor with ID for loading from files
    public MedicalFacility(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor without ID for new instances
    public MedicalFacility(String name) {
        this.name = name; // No ID generation here
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}
