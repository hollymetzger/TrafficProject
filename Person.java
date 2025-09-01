public class Person {

    // Private fields
    private Location home, destination;

    // data analytic fields
    private double leftHouseTime; // when they left their house
    private double busStopTime; // when they arrived at the bus stop
    private double homeBusTime; // when they got on the first bus
    private double homeTrainStationTime; // when they arrived at first train station
    private double trainTime; // when they boarded the train
    private double destinationTrainStationTime; // when they arrived at the destination train station
    private double destinationBusTime; // when they boarded the destination bus
    private double destinationBusStopTime; // when they arrive at the final bus stop
    private double destinationArrivalTime; // when they reached their final destination

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
                timeWaiting + "," + timeOnHomeBus + "," +
                timeOnTrain + "," + timeOnEndBus + "," + totalTimeInSystem;
    }

    // Mutators
    public void setLeftHouseTime(double time) { this.leftHouseTime = time; }
    public void setBusStopTime(double currentTime) { this.busStopTime = currentTime; }
    public void setHomeBusTime(double currentTime) { this.homeBusTime = currentTime; }
    public void setHomeTrainStationTime(double currentTime) { this.homeTrainStationTime = currentTime; }
    public void setTrainTime(double currentTime) { this.trainTime = currentTime; }
    public void setDestinationTrainStationTime(double currentTime) { this.destinationTrainStationTime = currentTime; }
    public void setDestinationBusTime(double currentTime) { this.destinationBusTime = currentTime; }
    public void setDestinationBusStopTime(double currentTime) { this.destinationBusStopTime = currentTime; }
    public void setDestinationArrivalTime(double time) { this.destinationArrivalTime = time; }

    // private methods
    private void calculate() {
        timeOnHomeBus = homeTrainStationTime - homeBusTime;
        timeOnTrain = destinationTrainStationTime - trainTime;
        timeOnEndBus = destinationBusStopTime - destinationBusTime;
        timeWaiting = (homeBusTime - busStopTime) +
                (trainTime - homeTrainStationTime) +
                (destinationBusTime - destinationTrainStationTime);
        totalTimeInSystem = destinationArrivalTime - leftHouseTime;
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

        p.setLeftHouseTime(0.0);
        p.setBusStopTime(10.0);
        p.setHomeBusTime(15.0);
        p.setHomeTrainStationTime(25.0);
        p.setTrainTime(35.0);
        p.setDestinationTrainStationTime(65.0);
        p.setDestinationBusTime(75.0);
        p.setDestinationBusStopTime(85);
        p.setDestinationArrivalTime(90.0);

        p.calculate();

        System.out.println("p. tostring: " + p.toString());

        if (!p.toString().equals("(0.0, 0.0),(10.0, 10.0),25.0,10.0,30.0,10.0,90.0")) {
            System.out.println("FAIL: p.toString should be");
            System.out.println("(0.0, 0.0),(10.0, 10.0),25.0,10.0,30.0,10.0,90.0 NOT");
            System.out.println(p.toString());
            failCount++;
        }
        testCount++;

        System.out.printf("Person tests passed: %d/%d\n",testCount-failCount, testCount);

        System.out.println("total time in system befofe calculating: " + p.totalTimeInSystem);
    }
}