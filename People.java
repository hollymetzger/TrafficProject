public class People {

    // Private fields
    private Person[] people;
    private ExponentialDistribution spawnTimeRNG;
    private int locationRNG;

    // Constructor
    public People(int numberOfPeople) {
        this.people = new Person[numberOfPeople];
    }

    // Public Methods
    public double update(double currentTime, double dt) {
        double timeUntilNextEvent = 1000000.0;
        for (int i = 0; i < this.people.length; i++) {
            timeUntilNextEvent = Math.min(timeUntilNextEvent, this.people[i].update(currentTime, dt));
        }
        return timeUntilNextEvent;
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running People tests");

        int failCount = 0;
        int testCount = 0;

        People people = new People(2);
        people.people[0] = new Person(new Location(0, 0), new Location(10, 10));
        people.people[1] = new Person(new Location(0, 0), new Location(20, 20));

        // Currently, person.update is hard coded to return 1.0, so test that
        if (people.update(1.0,1.0) != 1.0) {
            System.out.println("People test failed");
            failCount++;
        }
        testCount++;

        System.out.printf("People tests passed: %d/%d\n",testCount-failCount, testCount);

    }
}