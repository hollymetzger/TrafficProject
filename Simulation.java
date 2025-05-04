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

    // Fields used while running simulation
    private double currentTime = 0;
    private boolean isFinished;
    private Person[] finishedPeople;
    private double timeOfNextArrival;

    // Constructor
    public Simulation(
            int numberOfBuses, int numberOfTrains,
            double timeBetweenTrains,
            int trainSpeed,
            double maxTimeOnBus, double maxTimeWaitingForBus,
            String citiesCSV,
            String metroStopsCSV
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
        trains = new Trains(numberOfTrains, timeBetweenTrains, metroStopsCSV);
        cities.importFromCSV(citiesCSV);

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
        finishedPeople = new Person[NUMBEROFPEOPLE];
        timeOfNextArrival = setNextArrivalTime(currentTime);
    }
    // Accessors
    public boolean getFinished() {
        return isFinished;
    }

    // Public Methods

    public void run() {
        double dt = timeOfNextArrival; // set first dt to pass into update
        while (!isFinished) {
            dt = update(currentTime, dt);
        }
        // todo: export data
    }

    // Advance the simulation by dt, and return the time until next event after that
    double update(double currentTime, double dt) {
        currentTime += dt;

        // add commuters to the simulation
        if (currentTime >= timeOfNextArrival) {
            cities.generateCommuter();
            timeOfNextArrival = setNextArrivalTime(currentTime);
        }

        // determine the time of the next event in the simulation
        double timeUntilNextEvent = Math.min(
            timeOfNextArrival,
            cities.update(currentTime, dt),
            trains.update()
        );

        // check if simulation is finished
        if (finishedPeople.length == NUMBEROFPEOPLE) {
            isFinished = true;
        }
        return timeUntilNextEvent;
    }


    // Private Methods
    private double setNextArrivalTime(double currentTime) {
        // todo: exponentially determine next arriival time, where average time
        //  between arrivals decreases as time goes on, then goes back up toward the end
        return currentTime + 1.0;
    }
}
