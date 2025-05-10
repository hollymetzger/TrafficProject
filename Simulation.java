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
    double BUSSPEED, TRAINSPEED;
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
            double busSpeed, int busCapacity,
            double timeBetweenTrains,
            double trainSpeed, int trainCapacity,
            double maxTimeOnBus, double maxTimeWaitingForBus,
            String citiesCSV,
            String trainStopsCSV // todo: import train stop locations
    ) {
        System.out.println("Initializing Simulation with the following parameters:\n" +
                numberOfBuses + " buses\n" +
                numberOfTrains + " trains\n" +
                distanceBetweenBusStops + " distance between bus stops\n" +
                "City data from " + citiesCSV);

        // Set parameters
        NUMBEROFBUSES = numberOfBuses;
        NUMBEROFTRAINS = numberOfTrains;
        DISTANCEBETWEENBUSSTOPS = distanceBetweenBusStops;
        TIMEBETWEENTRAINS = timeBetweenTrains;
        TRAINSPEED = trainSpeed;
        MAXTIMEONBUS = maxTimeOnBus;
        MAXTIMEWAITINGFORBUS = maxTimeWaitingForBus;

        // Initialize objects
        System.out.println("Initializing sim objects");

        FrederickTrainStop = new Stop(45,35);
        System.out.println("Frederick train stop created at " + FrederickTrainStop.toString());

        trains = new Trains(numberOfTrains, timeBetweenTrains, trainSpeed, trainCapacity, FrederickTrainStop);
        System.out.println("Trains created:\n" + trains);

        fredrickCities = new Cities(citiesCSV, FrederickTrainStop, DISTANCEBETWEENBUSSTOPS, numberOfBuses, busSpeed, busCapacity);
        System.out.println("Frederick cities created: ");
        System.out.println(fredrickCities.printCities());
        NUMBEROFPEOPLE = fredrickCities.getTotalPopulation(); // have to set this after cities are loaded
        arrivalTimeRNG = new ExponentialDistribution(arrivalTimeLambda);

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
            isFinished = true;
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
