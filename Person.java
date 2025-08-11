public class Person {

    // Private fields
    private Location home, destination;

    // data analytic fields
    private double homeWalkTime; // the time it took for a person to walk to the bus stop nearest their home location
    private double busStopTime; // when they arrived at the bus stop
    private double homeBusTime; // when they got on the first bus
    private double homeTrainStationTime; // when they arrived at first train station
    private double trainTime; // when they boarded the train
    private double destinationTrainStationTime; // when they arrived at the destination train station
    private double destinationBusTime; // when they boarded the destination bus
    private double destinationBusStopTime; // when they arrive at the final bus stop
    private double destinationWalkTime; // the time it took for this to walk from bus stop to final location

    private double timeOnHomeBus; // the time they spent riding the start bus
    private double timeOnTrain; // the time they spent riding the train
    private double timeOnEndBus; // the time they spent riding the second bus
    private double timeWaiting; // the time they spent waiting at stops
    private double totalTimeInSystem;

    private String homeCity;
    private String destinationCity;

    // Constructor
    public Person(Location home, Location destination, String homeCity, String destCity) {
        this.home = home;
        this.destination = destination;
        this.homeCity = homeCity;
        this.destinationCity = destCity;
    }

    // Accessors
    public Location getHome() {
        return home;
    }
    public Location getDestinationLoc() {
        return destination;
    }
    public String getDestinationCity() {
        return destinationCity;
    }
    public double getTimeOnStartBus() {
        return timeOnHomeBus;
    }

    // toString prints in csv format for analytics
    public String toString() {
        return home.toString() + "," + destination.toString() + "," +
                totalTimeInSystem + "," + timeOnHomeBus*2 + "," +      // todo: when i add the end bus portion use that
                timeOnTrain;                                            //  instead of multiplying start bus by 2
    }

    // Mutators
    public void setHomeWalkTime(double time) { this.homeWalkTime = time; }
    public void setBusStopTime(double currentTime) { this.busStopTime = currentTime; }
    public void setHomeTrainStationTime(double currentTime) { this.homeTrainStationTime = currentTime; }
    public void setTrainTime(double currentTime) { this.trainTime = currentTime; }
    public void setDestinationTrainStationTime(double currentTime) { this.destinationTrainStationTime = currentTime; }
    public void setDestinationBusTime(double currentTime) { this.destinationBusTime = currentTime; }
    public void setDestinationBusStopTime(double currentTime) { this.destinationBusStopTime = currentTime; }
    public void setDestinationWalkTime(double time) { this.destinationWalkTime = time; }

    // private methods
    private void calculate() {
        timeOnHomeBus = homeTrainStationTime - homeBusTime;
        timeOnTrain = destinationTrainStationTime - trainTime;
        timeOnEndBus = destinationBusStopTime - destinationBusTime;
        timeWaiting = (homeBusTime - busStopTime) +
                (trainTime - homeTrainStationTime) +
                (destinationBusTime - destinationTrainStationTime);
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Person tests");

        int failCount = 0;
        int testCount = 0;

        Person p = new Person(new Location(0, 0), new Location(10, 10), "home", "destination");
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
        p.setBusStopTime(10.0);
        p.setDestinationBusStopTime(60.0);


        System.out.printf("Person tests passed: %d/%d\n",testCount-failCount, testCount);

        System.out.println("total time in system befofe calculating: " + p.totalTimeInSystem);
    }
}