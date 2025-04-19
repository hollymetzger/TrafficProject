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

    public boolean isEqual(Location other) {
        return (this.getX() == other.getX() && this.getY() == other.getY());
    }

    public Location getNearest(Location[] locs) {
        Location closest = locs[0];
        double distance = this.getDistance(locs[0]);
        for (int i = 1; i < locs.length; i++) {
            if (this.getDistance(locs[i]) < distance) {
                distance = this.getDistance(locs[i]);
                closest = locs[i];
            }
        }
        return closest;
    }
    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Location tests");

        int testCount = 0;
        int failCount = 0;

        Location l1 = new Location(0, 0);
        Location l2 = new Location(3, 4);

        if (l1.getX() != 0.0) {
            System.out.println("Fail: l1 x should be 0.0");
            failCount++;
        }
        testCount++;
        if (l1.getDistance(l2) != 5.0) {
            System.out.println("Fail: distance should be 5.0");
            failCount++;
        }
        testCount++;

        System.out.printf("Location tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}
