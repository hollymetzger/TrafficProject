public class Bus extends Vehicle {
    private double timeToMetroStation;
    private double distanceToNextStop;
    private double timeSinceLastStop;
    private Stop nextStop;
    private Stop train;


    // data analytic fields
    private double totalDistanceTraveled;
    private String homeCity;

    // Constructor
    public Bus(double speed, int maxCapacity, Stop startLocation) {
        super(speed, maxCapacity);
        this.nextStop = startLocation;
    }

    // Accessors
    public double getTimeToMetroStation() {
        return timeToMetroStation;
    }
    public Stop getNextStop() {
        return nextStop;
    }
    public String toString() {
        return super.toString() + "\n" +
               "Next stop: " + nextStop.toString();
    }

    // Public methods

    // Returns the Stop the bus will travel to next
    public Stop determineNextStop(BusStops stops) {
        return determineNextStopPlaceholder(stops);
    }

    // Using the location determined above, calculate distance and set this.nextStop
    public void setNextStop(Stop ns) {
        if (ns == null) {
            System.out.println("attempted to set next stop to null");
        } else {
            System.out.println("setting next stop to " + ns);
        }

        Stop currentStop = this.nextStop;
        this.nextStop = ns;
        this.distanceToNextStop = currentStop.getDistance(ns);
    }

    // Advances the bus in the simulation by dt time
    public double update(double currentTime, double dt, BusStops stops) {

        System.out.println("updating bus");
        double distance = speed * dt;
        distanceToNextStop = Math.max(0, distanceToNextStop - distance);
        totalDistanceTraveled += distance;

        // if we have reached the stop, unload passengers and determine next stop
        if (distanceToNextStop == 0) {
            System.out.println("bus is at stop " + nextStop.toString());
            if (nextStop.isTrain()) {
                dropOff(nextStop);
            } else {
                System.out.println("bus picking up " + nextStop.getLine().getLength() + " passengers");
                pickUp(nextStop.getLine());
            }

            // set the next stop
            setNextStop(determineNextStop(stops));

            // update passengers on bus
            Node<Person> passenger = passengers.getHead();
            while (passenger != null) {
                passenger.getData().update(currentTime, dt);
                passenger = passenger.getNext();
            }

        }
        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        return distanceToNextStop / speed; // return the time it will take to reach next stop
    }

    // Private Methods

    // Simply goes to stop with most passengers
    private Stop determineNextStopPlaceholder(BusStops stops) {

        System.out.println("current stop is " + this.getNextStop());
        // check if we need to go to the train station
        if (currentCapacity == maxCapacity ||
                passengers.getHead().getData().getTimeOnStartBus() >= Parameters.MAXTIMEONBUS
        ) {
            return train;
        }

        // Find stop with most amount of passengers in its queue
        int highestPassCount = 0;
        Stop next = this.getNextStop(); // if the loop doesn't find any stops with > 0 passengers, it will not move on this loop
        for (Stop stop : stops.getStops()) {
            System.out.println(stop + " has " + stop.getLineLength() + " passengers waiting");
            if (stop.getLine().getLength() > highestPassCount) {
                next = stop;
                highestPassCount = stop.getLine().getLength();
            }
        }
        System.out.println("next stop will be " + next);
        return next;
    }

    private Stop determineNextStopSmartly(BusStops stops) {
        if (currentCapacity == maxCapacity ||
            passengers.getHead().getData().getTimeOnStartBus() >= Parameters.MAXTIMEONBUS
        ) {
            return train;
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

        Bus b = new Bus(25, 5, new Stop(0, 0));

        if (b.getNextStop().getX() != 0) {
            System.out.println("Fail: next stop should be 0");
            failCount++;
        }
        testCount++;

        b.setNextStop(new Stop(22, 22));
        if (b.getNextStop().getX() != 22) {
            System.out.println("Fail: next stop should be 22");
            failCount++;
        }
        testCount++;

        Queue<Person> people = new Queue<Person>();
        for (int i=0; i < 5; i++) {
            people.enqueue(new Person(new Location(i*1.0,1*1.0), new Location(i*2.0,1*2.0), "home"));
        }
        b.pickUp(people);
        b.train = new Stop(99.9,99.9);
        BusStops stops = new BusStops(10,1, b.train);

        // test going to metro if bus is full
        if (b.determineNextStop(stops).getX() != 99.9) {
            System.out.println("Fail: next stop should be metro");
            failCount++;
        }
        testCount++;

        System.out.printf("Bus tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}
