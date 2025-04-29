public class Bus extends Vehicle {
    private double timeToMetroStation;
    private double distanceToNextStop;
    private double timeSinceLastStop;
    private BusStop nextStop;
    private BusStop metro;

    // data analytic fields
    private double totalDistanceTraveled;

    // Constructor
    public Bus(double speed, int maxCapacity, BusStop startLocation) {
        super(speed, maxCapacity);
        this.nextStop = startLocation;
    }

    // Accessors
    public double getTimeToMetroStation() {
        return timeToMetroStation;
    }
    public Location getNextStop() {
        return nextStop;
    }
    public String toString() {
        return super.toString() + "\n" +
               "Next stop: " + nextStop.toString();
    }

    // Public methods

    // Returns the BusStop the bus will travel to next
    public BusStop determineNextStop(BusStops stops) {
        return determineNextStopPlaceholder();
    }

    // Using the location determined above, calculate distance and set this.nextStop
    public void setNextStop(BusStop ns) {
        BusStop currentStop = this.nextStop;
        this.nextStop = ns;
        this.distanceToNextStop = currentStop.getDistance(ns);
    }

    // Advances the bus in the simulation by dt time
    public double update(double currentTime, double dt) {
        timeSinceLastStop += dt;

        double distance = speed*dt;
        distanceToNextStop -= distance;
        totalDistanceTraveled += distance;

        // if we have reached the stop, unload passengers and determine next stop
        if (distanceToNextStop == 0) {
            // todo: pickup and drop off passengers

            // check if bus needs to go to metro next
            for (int i = 0; i < currentCapacity; i++) {
                Person person = passengers[i];
                if (person.getTimeOnStartBus() >= Parameters.MAXTIMEONBUS) {
                    this.setNextStop(metro);
                }
            }
            this.setNextStop(this.determineNextStop());
        }

        // update passengers on bus
        for (int i = 0; i < currentCapacity; i++) {
            Person person = passengers[i];
            person.update(currentTime, timeSinceLastStop, nextStop);
        }

        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        return distanceToNextStop/speed; // return the time it will take to reach next stop
    }

    // Private Methods
    private BusStop determineNextStopPlaceholder() {
        return new BusStop(1.0,1.0);
    }

    private BusStop determineNextStopSmartly() {
        if (currentCapacity == maxCapacity) {
            return metro;
        }
        // todo: determine the next stop by comparing the bus stops' number of passengers,
        //  longest wait time, and sum of wait times at the bus stop
        return null;
    }

    // Unit testing method
    public static void doUnitTests() {
        System.out.println("Running Bus Tests");

        int testCount = 0;
        int failCount = 0;

        Bus b = new Bus(25, 5, new BusStop(0, 0));

        if (b.getNextStop().getX() != 0) {
            System.out.println("Fail: next stop should be 0");
            failCount++;
        }
        testCount++;

        b.setNextStop(new BusStop(22, 22));
        if (b.getNextStop().getX() != 22) {
            System.out.println("Fail: next stop should be 22");
            failCount++;
        }
        testCount++;

        Person[] people = new Person[5];
        for (int i=0; i < 5; i++) {
            people[i] = new Person(new Location(i*1.0,1*1.0), new Location(i*2.0,1*2.0));
        }
        b.pickUp(people);
        b.metro = new BusStop(99.9,99.9);

        // test going to metro if bus is full
        if (b.determineNextStop().getX() != 99.9) {
            System.out.println("Fail: next stop should be metro");
            failCount++;
        }
        testCount++;

        System.out.printf("Bus tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}
