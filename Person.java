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
    public String toString() {
        return "Home: " +home.toString() +
                "\nDestination: " + destination.toString();
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
        Person p = new Person(new Location(0, 0), new Location(10, 10));
        System.out.println(p);
    }
}