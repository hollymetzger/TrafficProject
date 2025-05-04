import java.util.ArrayList;

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

    public <T extends Location> T getNearest(ArrayList<T> locs) {
        T closest = locs.getFirst();
        double distance = this.getDistance(closest);

        for (T loc : locs) {
            if (this.getDistance(loc) < distance) {
                distance = this.getDistance(loc);
                closest = loc;
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

        ArrayList<Location> locs = new ArrayList<Location>();
        locs.add(new Location(0,0));
        locs.add(new Location(-1,-1));
        locs.add(new Location(1,1));

        Location closest = l2.getNearest(locs);
        if (!closest.isEqual(new Location(1,1))) {
            System.out.println("Fail: closest location should be (1,1)");
            failCount++;
        } testCount++;

        System.out.printf("Location tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}
