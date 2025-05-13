public class Bus extends Vehicle {
    private double timeToMetroStation;
    private double distanceToNextStop;
    private double timeSinceLastStop;
    private Stop nextStop;
    private Stop train;
    private double maxTimeOnBus;
    private boolean idle;

    // data analytic fields
    private double totalDistanceTraveled;
    private String homeCity;

    // Constructor
    public Bus(double speed, int maxCapacity, Stop startLocation, Stop tr) {
        super(speed, maxCapacity);
        currentCapacity = 0;
        this.nextStop = startLocation;
        train = tr;
        maxTimeOnBus = 15;
        idle = true;
        // System.out.println("constructing bus, placing at stop " + nextStop.toString());
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
        return determineNextStopSmartly(stops);
    }

    // Using the location determined above, calculate distance and set this.nextStop
    public void setNextStop(Stop ns) {
        if (ns == null) {
            // System.out.println("attempted to set next stop to null");
        } else if (ns.isEqual(this.nextStop)) {
            idle = true;
        } else {
            // System.out.println("setting next stop to " + ns);
            ns.setBusIncoming(true);
            Stop currentStop = this.nextStop;
            this.nextStop = ns;
            this.distanceToNextStop = currentStop.getDistance(ns);
            idle = false;
        }


    }

    // Advances the bus in the simulation by dt time
    public double update(double currentTime, double dt, BusStops stops) {

        if (distanceToNextStop > 0) {
            double distance = speed * dt;
            distanceToNextStop = Math.max(0, distanceToNextStop - distance);
            totalDistanceTraveled += distance;
        }
        // if we have reached the stop, unload passengers and determine next stop
        if (distanceToNextStop == 0) {
            if (nextStop.isTrain()) {
                dropOff(nextStop);
                System.out.println("dropping off at " + nextStop);
            } else {
                pickUp(nextStop.getLine());
                System.out.println("Picking up at " + nextStop);
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
        if (idle) {
            // System.out.println("bus not moving this loop");
            return Double.POSITIVE_INFINITY;
        } else {
            // System.out.println("Bus reaching next stop in " + distanceToNextStop / speed);
            return distanceToNextStop / speed; // return the time it will take to reach next stop
        }
    }

    // Private Methods

    // Simply goes to stop with most passengers
    private Stop determineNextStopPlaceholder(BusStops stops) {
        // check if we need to go to the train station
        if (currentCapacity == maxCapacity || getLongestTimeOnBus() >= maxTimeOnBus
        ) {
            // System.out.println("returning train");
            return train;
        }

        // Find stop with most amount of passengers in its queue
        int highestPassCount = 0;
        Stop next = this.getNextStop(); // if the loop doesn't find any stops with > 0 passengers, it will not move on this loop
        for (Stop stop : stops.getStops()) {
            if (stop.getLine().getLength() > highestPassCount && !stop.getBusIncoming()) {
                next = stop;
                highestPassCount = stop.getLine().getLength();
            }
        }
        return next;
    }

    private Stop determineNextStopSmartly(BusStops stops) {
        // check if we need to go to the train station
        if (currentCapacity == maxCapacity || getLongestTimeOnBus() >= maxTimeOnBus
        ) {
            System.out.println("going to train station");
            return train;
        }

        // Find stop with most amount of passengers in its queue
        int highestPassCount = 0;
        Stop next = this.getNextStop(); // if the bus is empty and the loop doesn't find any stops with > 0 passengers, it will not move on this loop
        for (Stop stop : stops.getStops()) {
            if (stop.getLine().getLength() > highestPassCount && !stop.getBusIncoming()) {
                next = stop;
                highestPassCount = stop.getLine().getLength();
            }
        }
        if (highestPassCount == 0 && !passengers.isEmpty()) {
            System.out.println("going to train station");
            return train;
        }
        return next;
    }

    private double getLongestTimeOnBus() {
        if (passengers.getHead() == null) {
            return 0;
        } else {
            return passengers.getHead().getData().getTimeOnStartBus();
        }
    }

    // Unit testing method
    public static void doUnitTests() {
        // System.out.println("Running Bus Tests");

        int testCount = 0;
        int failCount = 0;

        Bus b = new Bus(25, 5, new Stop(0, 0), new Stop(10,10));

        if (b.getNextStop().getX() != 0) {
            // System.out.println("Fail: next stop should be 0");
            failCount++;
        }
        testCount++;

        b.setNextStop(new Stop(22, 22));
        if (b.getNextStop().getX() != 22) {
            // System.out.println("Fail: next stop should be 22");
            failCount++;
        }
        testCount++;

        Queue<Person> people = new Queue<Person>();
        for (int i=0; i < 5; i++) {
            people.enqueue(new Person(new Location(i*1.0,1*1.0), new Location(i*2.0,1*2.0), "home"));
        }
        b.pickUp(people);
        b.train = new Stop(99.9,99.9);
        BusStops stops = new BusStops(10.0,1.0, b.train, new Location(0,0));

        // test going to metro if bus is full
        if (b.determineNextStop(stops).getX() != 99.9) {
            // System.out.println("Fail: next stop should be metro");
            failCount++;
        }
        testCount++;

        System.out.printf("Bus tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}
