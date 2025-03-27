public class Location {

    // Private fields
    private double x, y;

    // Constructor
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Accessors
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Location tests");
    }
}
