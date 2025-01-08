package object;

public class Clinic extends MedicalFacility {

    private static int idCounter = 1; // Static counter for Clinic IDs
    private double fee;
    private double gapPercent;

    // Constructor with ID for loading from files
    public Clinic(int id, String name, double fee, double gapPercent) {
        super(id, name);
        this.fee = fee;
        this.gapPercent = gapPercent;
    }

    // Constructor without ID for new instances
    public Clinic(String name, double fee, double gapPercent) {
        super(name);
        this.fee = fee;
        this.gapPercent = gapPercent;
        this.id = generateId(); // Generate a new ID
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getGapPercent() {
        return gapPercent;
    }

    public void setGapPercent(double gapPercent) {
        this.gapPercent = gapPercent;
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
        return "Clinic {" + super.toString() + ", Fee: " + fee + ", Gap Percent: " + gapPercent + "}";
    }
}
