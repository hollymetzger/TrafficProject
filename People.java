public class People {

    // Private fields
    private Person[] people;
    private ExponentialDistribution spawnTimeRNG;
    private LocationRNG locationRNG;

    // Constructor
    public People(int numberOfPeople) {
        this.people = new Person[numberOfPeople];
    }

    // Public Methods
    public double update(double currentTime, double dt) {
        double timeUntilNextEvent = 10000.0;
        for (int i = 0; i < this.people.length; i++) {
            timeUntilNextEvent = Math.min(timeUntilNextEvent, this.people[i].update(currentTime, dt));
        }
        return timeUntilNextEvent;
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running People tests");
    }
}