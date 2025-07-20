public class Person {

    // Private fields
    private Location home, destination;

    // data analytic fields
    private double busStopTime; // when they arrived at the bus stop
    private double homeBusTime; // when they got on the first bus
    private double homeTrainStationTime; // when they arrived at first train station
    private double trainTime; // when they boarded the train
    private double destinationTrainStationTime; // when they arrived at the destination train station
    private double destinationBusTime; // when they boarded the destination bus
    private double destinationBusStopTime; // when they arrive at the final bus stop
    private double timeOnStartBus; // the time they spent riding the start bus
    private double timeOnTrain; // the time they spent riding the train
    private double timeOnEndBus; // the time they spent riding the second bus
    private double totalTimeInSystem;

    private String homeCity; // home city is only used for printing data at the end, so we just store it as a string
    private String destinationTrainStop; // destination city is used in the code, so we use an enum to reduce errors

    // Constructor
    public Person(Location home, Location destination, String homeCity) {
        this.home = home;
        this.destination = destination;
        this.homeCity = homeCity;
        this.destinationTrainStop = TrainStop.getWeightedStop();
    }

    // Accessors
    public Location getHome() {
        return home;
    }
    public Location getDestinationLoc() {
        return destination;
    }
    public TrainStop getDestinationTrainStop() {
        return destinationTrainStop;
    }
    public double getTimeOnStartBus() {
        return timeOnStartBus;
    }

    // exports in csv format for analytics
    public String toString() {
        return home.toString() + "," + destination.toString() + "," +
                totalTimeInSystem + "," + timeOnStartBus*2 + "," +      // todo: when i add the end bus portion use that
                timeOnTrain;                                            //  instead of multiplying start bus by 2
    }

    // Mutators
    public void addTime(double time) {
        totalTimeInSystem += time;
    }

    // Public Methods
    public void update(double currentTime, double dt) {
        totalTimeInSystem += dt;
        // todo: update time tracking fields
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Person tests");

        int failCount = 0;
        int testCount = 0;

        Person p = new Person(new Location(0, 0), new Location(10, 10), "home");
        if (!p.getHome().toString().equals("(0.0, 0.0)")) {
            failCount++;
            System.out.println("FAIL: person home should have been (0.0, 0.0)");
        }
        testCount++;
        if (!p.getDestinationLoc().toString().equals("(10.0, 10.0)")) {
            failCount++;
            System.out.println("FAIL: person destination should have been (10.0, 10.0)");
        }
        testCount++;

        System.out.printf("Person tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}