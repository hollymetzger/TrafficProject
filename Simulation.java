import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Simulation {
    // Parameters for Simulation
    int NUMBEROFPEOPLE, NUMBEROFBUSES, NUMBEROFTRAINS;
    double TIMEBETWEENTRAINS;
    int BUSSPEED, TRAINSPEED;
    double MAXTIMEONBUS, MAXTIMEWAITINGFORBUS;

    // Object holders
    private Cities cities;
    private Trains trains;
    private ExponentialDistribution arrivalTimeRNG;

    // Fields used while running simulation
    private double currentTime = 0;
    private boolean isFinished;
    private Person[] finishedPeople;
    private int commuterCount; // the number of people who have been generated so far
    private double timeUntilNextArrival;
    private double arrivalTimeLambda;

    // Constructor
    public Simulation(
            int numberOfBuses, int numberOfTrains,
            double timeBetweenTrains,
            int trainSpeed,
            double maxTimeOnBus, double maxTimeWaitingForBus,
            String citiesCSV,
            String trainStopsCSV
    ) {
        // Set parameters
        NUMBEROFPEOPLE = cities.getTotalPopulation();
        NUMBEROFBUSES = numberOfBuses;
        NUMBEROFTRAINS = numberOfTrains;
        TIMEBETWEENTRAINS = timeBetweenTrains;
        TRAINSPEED = trainSpeed;
        MAXTIMEONBUS = maxTimeOnBus;
        MAXTIMEWAITINGFORBUS = maxTimeWaitingForBus;

        // Initialize objects
        trains = new Trains(numberOfTrains, timeBetweenTrains, trainStopsCSV);
        cities.importFromCSV(citiesCSV);
        arrivalTimeRNG = new ExponentialDistribution(arrivalTimeLambda);

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
        finishedPeople = new Person[NUMBEROFPEOPLE];
        timeUntilNextArrival = setTimeUntilNextArrival();
        commuterCount = 0;
    }
    // Accessors
    public boolean getFinished() {
        return isFinished;
    }

    // Public Methods

    public void run() {
        double dt = timeUntilNextArrival; // set first dt to pass into update
        while (!isFinished) {
            dt = update(currentTime, dt);
        }
        // todo: export people and vmt data
    }

    // Advance the simulation by dt, and return the time until next event after that
    double update(double currentTime, double dt) {
        currentTime += dt;
        timeUntilNextArrival = Math.max(0, timeUntilNextArrival-dt);

        // add commuters to the simulation
        if (timeUntilNextArrival == 0) {
            cities.generateCommuter();
            timeUntilNextArrival = setTimeUntilNextArrival();
        }

        // determine the time of the next event in the simulation
        double timeUntilNextEvent = Math.min(Math.min(
                timeUntilNextArrival,
                cities.update(currentTime, dt)),
                trains.update(currentTime, dt)
        );

        // check if simulation is finished
        if (finishedPeople.length == NUMBEROFPEOPLE) {
            isFinished = true;
        }
        return timeUntilNextEvent;
    }

    // Private Methods
    private double setTimeUntilNextArrival() {
        // the rate of arrivals gradually increases until halfway through, then it decreases again
        if (commuterCount <= NUMBEROFPEOPLE/2) {
            arrivalTimeLambda *= 1.0005;
        } else {
            arrivalTimeLambda *= 0.9995;
        }
        return arrivalTimeRNG.sample(arrivalTimeLambda);
    }
}
