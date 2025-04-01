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
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // Public methods
    public double getDistance(Location location) {
        return Math.sqrt(Math.pow(this.x - location.getX(), 2) + Math.pow(this.y - location.getY(), 2));
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Location tests");
    }
}
