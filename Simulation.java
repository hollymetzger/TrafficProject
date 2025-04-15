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
    private Person[] finishedPeople;


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
        finishedPeople = new Person[numberOfPeople];
    }

    // Advance the simulation by dt, and return the time until next event after that
    double update(double currentTime, double dt) {
        currentTime += dt;
        return Math.min(
            people.update(currentTime, dt),
            buses.update(),
            trains.update()
        );
    }
}
