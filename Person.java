public class Person {

    // Private fields
    private Location home, destination;

    // Constructor
    public Person(Location home, Location destination) {
        this.home = home;
        this.destination = destination;
    }

    // Accessors
    public Location getHome() {
        return home;
    }
    public Location getDestination() {
        return destination;
    }

    // Public Methods
    public boolean ridesTransit() {
        return true;
    }

    public double update(double currentTime, double dt) {
        return 1.0;
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Person tests");
    }
}