public class Person {

    // Private fields
    private Location home, destination;

    // data analytic fields
    private double timeInSystem;
    private double timeOnStartBus;

    // Constructor
    public Person(Location home, Location destination) {
        this.home = home;
        this.destination = destination;
        // todo: add time in simulation for distance walked from home to nearest bus stop
    }

    // Accessors
    public Location getHome() {
        return home;
    }
    public Location getDestination() {
        return destination;
    }
    public double getTimeOnStartBus() {
        return timeOnStartBus;
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
        timeInSystem += dt;
        return 1.0;
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Person tests");

        int failCount = 0;
        int testCount = 0;

        Person p = new Person(new Location(0, 0), new Location(10, 10));
        if (!p.getHome().toString().equals("(0.0, 0.0)")) {
            failCount++;
            System.out.println("FAIL: person home should have been (0.0, 0.0)");
        }
        testCount++;
        if (!p.getDestination().toString().equals("(10.0, 10.0)")) {
            failCount++;
            System.out.println("FAIL: person destination should have been (10.0, 10.0)");
        }
        testCount++;

        System.out.printf("Person tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}