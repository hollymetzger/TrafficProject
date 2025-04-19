import java.util.ArrayList;

public class Simulation {
    // Parameters for Simulation
    int NUMBEROFPEOPLE, NUMBEROFBUSES, NUMBEROFTRAINS;
    double TIMEBETWEENTRAINS, DISTANCEBETWEENBUSSTOPS, FREDERICKRADIUS;
    int BUSSPEED, TRAINSPEED;
    double MAXTIMEONBUS, MAXTIMEWAITINGFORBUS;


    // Object holders
    private People people;
    private Buses buses;
    private Trains trains;
    private BusStops busStops;

    // Fields used while running simulation
    private double currentTime = 0;
    private boolean isFinished;
    private boolean justStarted;
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
        busStops = new BusStops(FREDERICKRADIUS, DISTANCEBETWEENBUSSTOPS);

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
        justStarted = true;
        finishedPeople = new Person[numberOfPeople];
        arrivalTimeRNG = new ExponentialDistribution(1.0);
        timeUntilNextArrival = arrivalTimeRNG.sample();
    }

    // Advance the simulation by dt, and return the time until next event after that
    double update(double currentTime, double dt) {

        // todo: if just started, dt is timeUntilNextArrival
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

    private double setNextArrivalTime(double currentTime) {
        // todo: exponentially determine next arriival time, where average time
        //  between arrivals decreases as time goes on, then goes back up toward the end
        return arrivalTimeRNG.sample();
    }



}
