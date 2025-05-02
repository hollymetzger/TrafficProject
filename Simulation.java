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
    private Train[] trains;

    // Fields used while running simulation
    private double currentTime = 0;
    private boolean isFinished;
    private Person[] finishedPeople;

    // Constructor
    public Simulation(
            int numberOfBuses, int numberOfTrains,
            double timeBetweenTrains,
            int trainSpeed,
            double maxTimeOnBus, double maxTimeWaitingForBus,
            String citiesCSV
    ) {
        // Set parameters
        NUMBEROFPEOPLE = cities.getTotalPopulation();
        NUMBEROFBUSES = numberOfBuses;
        NUMBEROFTRAINS = numberOfTrains;
        TIMEBETWEENTRAINS = timeBetweenTrains;
        TRAINSPEED = trainSpeed;
        MAXTIMEONBUS = maxTimeOnBus;
        MAXTIMEWAITINGFORBUS = maxTimeWaitingForBus;

        // Initialize object classes
        trains = new Trains(numberOfTrains);
        cities.importFromCSV(citiesCSV);

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
        finishedPeople = new Person[NUMBEROFPEOPLE];
    }
    // Accessors
    public boolean getFinished() {
        return isFinished;
    }

    // Public Methods

    public void run() {
        while (!isFinished) {
            update();
        }
    }

    // Advance the simulation by dt, and return the time until next event after that
    double update(double currentTime, double dt) {

        // todo: if just started, dt is timeUntilNextArrival
        currentTime += dt;
        double timeUntilNextEvent = Math.min(
            cities.update(currentTime, dt),
            trains.update()
        );

        if (finishedPeople.length == NUMBEROFPEOPLE) {
            isFinished = true;
        }
        return timeUntilNextEvent;
    }


    // Private Methods
    private double setNextArrivalTime(double currentTime) {
        // todo: exponentially determine next arriival time, where average time
        //  between arrivals decreases as time goes on, then goes back up toward the end
        return arrivalTimeRNG.sample();
    }
}
