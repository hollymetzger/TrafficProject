import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Simulation {
    // Parameters for Simulation
    int NUMBEROFPEOPLE, NUMBEROFBUSES, NUMBEROFTRAINS;
    double DISTANCEBETWEENBUSSTOPS, TIMEBETWEENTRAINS;
    int BUSSPEED, TRAINSPEED;
    double MAXTIMEONBUS, MAXTIMEWAITINGFORBUS;

    // Object holders
    private Cities fredrickCities;
    private Stop FrederickTrainStop;
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
            double distanceBetweenBusStops,
            double timeBetweenTrains,
            int trainSpeed,
            double maxTimeOnBus, double maxTimeWaitingForBus,
            String citiesCSV,
            String trainStopsCSV // todo: import train stop locations
    ) {

        // Initialize objects
        FrederickTrainStop = new Stop(45,35);
        trains = new Trains(numberOfTrains, timeBetweenTrains, FrederickTrainStop);
        Cities fredrickCities = new Cities(citiesCSV, FrederickTrainStop, DISTANCEBETWEENBUSSTOPS);
        arrivalTimeRNG = new ExponentialDistribution(arrivalTimeLambda);

        // Set parameters
        NUMBEROFPEOPLE = fredrickCities.getTotalPopulation();
        NUMBEROFBUSES = numberOfBuses;
        NUMBEROFTRAINS = numberOfTrains;
        DISTANCEBETWEENBUSSTOPS = distanceBetweenBusStops;
        TIMEBETWEENTRAINS = timeBetweenTrains;
        TRAINSPEED = trainSpeed;
        MAXTIMEONBUS = maxTimeOnBus;
        MAXTIMEWAITINGFORBUS = maxTimeWaitingForBus;

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
        finishedPeople = new Person[NUMBEROFPEOPLE];
        timeUntilNextArrival = setTimeUntilNextArrival();
        commuterCount = 0;
    }

    // Public Methods

    public void run() {
        System.out.println("Running simulation");
        double dt = timeUntilNextArrival; // set first dt to pass into update
        while (!isFinished) {
            dt = update(currentTime, dt);
        }
        // todo: export people and vmt data
    }

    // Advance the simulation by dt, and return the time until next event after that
    double update(double currentTime, double dt) {
        currentTime += dt;
        timeUntilNextArrival = Math.max(0, timeUntilNextArrival - dt);

        // add commuters to the simulation
        if (timeUntilNextArrival == 0) {
            fredrickCities.generateCommuter();
            commuterCount++;
            timeUntilNextArrival = setTimeUntilNextArrival();
        }

        // update objects and determine the time of the next event in the simulation
        double timeUntilNextEvent = Math.min(Math.min(
                timeUntilNextArrival,
                fredrickCities.update(currentTime, dt)),
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
