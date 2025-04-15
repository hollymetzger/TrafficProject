public class Simulation {
    // Parameters for Simulation
    int NUMBEROFPEOPLE, NUMBEROFBUSES, NUMBEROFTRAINS;
    double TIMEBETWEENTRAINS, DISTANCEBETWEENBUSSTOPS, FREDERICKRADIUS;
    int BUSSPEED, TRAINSPEED;
    double MAXTIMEONBUS, MAXTIMEWAITINGFORBUS;


    // Arrays to hold objects
    private People people;
    private Buses buses;
    private Trains trains;
    private Location[] busStops;

    // Fields used while running simulation
    private double currentTime = 0;
    private boolean isFinished;
    private Person[] finishedPeople;


    // Constructor
    public Simulation(
            int numberOfPeople, int numberOfBuses, int numberOfTrains,
            double timeBetweenTrains, double distanceBetweenBusStops, double frederickRadius,
            int busSpeed, int trainSpeed,
            double maxTimeOnBus, double maxTimeWaitingForBus
    ) {
        // Set parameters
        NUMBEROFPEOPLE = numberOfPeople;
        NUMBEROFBUSES = numberOfBuses;
        NUMBEROFTRAINS = numberOfTrains;
        TIMEBETWEENTRAINS = timeBetweenTrains;
        DISTANCEBETWEENBUSSTOPS = distanceBetweenBusStops;
        FREDERICKRADIUS = frederickRadius;
        BUSSPEED = busSpeed;
        TRAINSPEED = trainSpeed;
        MAXTIMEONBUS = maxTimeOnBus;
        MAXTIMEWAITINGFORBUS = maxTimeWaitingForBus;

        // Initialize object classes
        people = new People(numberOfPeople);
        buses = new Buses(numberOfBuses);
        trains = new  Trains(numberOfTrains);
        generateBusStops(FREDERICKRADIUS, DISTANCEBETWEENBUSSTOPS);

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
        finishedPeople = new Person[numberOfPeople];
    }

    // Advance the simulation by dt, and return the time until next event after that
    double update(double currentTime, double dt) {
        currentTime += dt;
        double timeUntilNextEvent = Math.min(
            people.update(currentTime, dt),
            buses.update(),
            trains.update()
        );

        if (finishedPeople.length == NUMBEROFPEOPLE) {
            isFinished = true;
        }
        return timeUntilNextEvent;
    }


    // Private methods

    private void generateBusStops(double radius, double distance) {
        int i = 0;
        for (double x = -radius; x <= radius; x += distance) {
            for (double y = -radius; y <= radius; y += distance) {
                if (x * x + y * y <= radius * radius) {
                    this.busStops[i] = new Location(x, y);
                    i++;
                }
            }
        }
    }
}
