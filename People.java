public class People {

    // Private fields
    private Person[] people;
    private ExponentialDistribution arrivalTimeRNG;
    private int locationRNG;
    private double timeUntilNextArrival;

    // Constructor
    public People(int numberOfPeople) {
        this.people = new Person[numberOfPeople];
    }

    // Public Methods

    // The People.update method handles arrivals to system
    public double update(double currentTime, double dt, BusStops stops) {
        timeUntilNextArrival = Math.max (0, timeUntilNextArrival-=dt);
        if (timeUntilNextArrival == 0) {
            generate(stops);
        }
        return timeUntilNextArrival;
    }

    public void generate(BusStops stops) {
        // todo: implement this
        //  need to create location weight data structure
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running People tests");

        int failCount = 0;
        int testCount = 0;

        People people = new People(2);
        Location loc0 = new Location(0, 0);
        people.people[0] = new Person(loc0, new Location(10, 10));
        people.people[1] = new Person(loc0, new Location(20, 20));

        // Currently, person.update is hard coded to return 1.0, so test that
        if (people.update(1.0,1.0, loc0) != 1.0) {
            System.out.println("People test failed");
            failCount++;
        }
        testCount++;

        System.out.printf("People tests passed: %d/%d\n",testCount-failCount, testCount);

    }
}