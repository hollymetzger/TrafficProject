public class Simulation {
    // Parameters for Simulation
    int NUMBEROFPEOPLE, NUMBEROFBUSES, NUMBEROFTRAINS;
    double TIMEBETWEENTRAINS, DISTANCEBETWEENBUSSTOPS;
    int BUSSPEED, TRAINSPEED;
    double MAXTIMEONBUS, MAXTIMEWAITINGFORBUS;


    // Arrays to hold objects
    private People people;
    private Buses buses;
    private Trains trains;

    // Fields used while running simulation
    private double currentTime = 0;
    private boolean isFinished;

    // Constructor
    public Simulation(
            int numberOfPeople, int numberOfBuses, int numberOfTrains,
            double timeBetweenTrains, double distanceBetweenBusStops,
            int busSpeed, int trainSpeed,
            double maxTimeOnBus, double maxTimeWaitingForBus
    ) {
        // Set parameters
        NUMBEROFPEOPLE = numberOfPeople;
        NUMBEROFBUSES = numberOfBuses;
        NUMBEROFTRAINS = numberOfTrains;
        TIMEBETWEENTRAINS = timeBetweenTrains;
        DISTANCEBETWEENBUSSTOPS = distanceBetweenBusStops;
        BUSSPEED = busSpeed;
        TRAINSPEED = trainSpeed;
        MAXTIMEONBUS = maxTimeOnBus;
        MAXTIMEWAITINGFORBUS = maxTimeWaitingForBus;

        // Declare object classes
        people = new People(numberOfPeople);
        buses = new Buses(numberOfBuses);
        trains = new  Trains(numberOfTrains);

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
    }

    double update(double currentTime, double dt) {
        double timeUntilNextEvent = Double.POSITIVE_INFINITY;

        for (int i = -0; i < buses.length; i++) {
            double time = buses[i].update(currentTime, dt);
            if (time < timeUntilNextEvent) {
                timeUntilNextEvent = time;
            }
        }

        return timeUntilNextEvent;
    }
}
