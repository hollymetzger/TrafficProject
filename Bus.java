public class Bus extends Vehicle {
    private double timeToMetroStation;
    private double distanceToNextStop;
    private double timeSinceLastStop;
    private Stop nextStop;
    private Stop train;

    // data analytic fields
    private double totalDistanceTraveled;

    // Constructor
    public Bus(double speed, int maxCapacity, Stop startLocation) {
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

    // Returns the Stop the bus will travel to next
    public Stop determineNextStop(BusStops stops) {
        return determineNextStopPlaceholder();
    }

    // Using the location determined above, calculate distance and set this.nextStop
    public void setNextStop(Stop ns) {
        Stop currentStop = this.nextStop;
        this.nextStop = ns;
        this.distanceToNextStop = currentStop.getDistance(ns);
    }

    // Advances the bus in the simulation by dt time
    public double update(double currentTime, double dt, BusStops stops) {

        double distance = speed*dt;
        distanceToNextStop = Math.max(0, distanceToNextStop-distance);
        totalDistanceTraveled += distance;

        // if we have reached the stop, unload passengers and determine next stop
        if (distanceToNextStop == 0) {
            if (nextStop.isTrain()) {
                // todo: drop off passengers
            } else {
                pickUp(nextStop.getLine());
            }

            // check if bus needs to go to train station next
            boolean nextIsTrain = false;
            for (int i = 0; i < currentCapacity; i++) {
                Person person = passengers[i];
                if (person.getTimeOnStartBus() >= Parameters.MAXTIMEONBUS) {
                    nextIsTrain = true;
                }
            }
            if (currentCapacity == maxCapacity) {
                nextIsTrain = true;
            }
            if (nextIsTrain) {
                setNextStop(train);
            } else {
                this.setNextStop(this.determineNextStop(stops));
            }
        }

        // update passengers on bus
        for (int i = 0; i < currentCapacity; i++) {
            Person person = passengers[i];
            person.update(currentTime, dt);
        }

        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        return distanceToNextStop/speed; // return the time it will take to reach next stop
    }

    // Private Methods
    private Stop determineNextStopPlaceholder() {
        return new Stop(1.0,1.0);
    }

    private Stop determineNextStopSmartly() {
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
            people.enqueue(new Person(new Location(i*1.0,1*1.0), new Location(i*2.0,1*2.0)));
        }
        b.pickUp(people);
        b.metro = new Stop(99.9,99.9);
        BusStops stops = new BusStops(10,1, b.metro);

        // test going to metro if bus is full
        if (b.determineNextStop(stops).getX() != 99.9) {
            System.out.println("Fail: next stop should be metro");
            failCount++;
        }
        testCount++;

        System.out.printf("Bus tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}
