import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/*

Notes:
Distance and time are arbitrary units, the parameters
I am giving it will measure them in miles and minutes, so
a train going 75 mph will have a speed of 1.25 miles/minute

 */



public class Simulation {
    // Parameters for Simulation
    int NUMBEROFPEOPLE, NUMBEROFBUSES, NUMBEROFTRAINS;
    double DISTANCEBETWEENBUSSTOPS;
    double BUSSPEED, TRAINSPEED;
    double MAXTIMEONBUS, MAXTIMEWAITINGFORBUS;

    // Object holders
    private Cities fredrickCities;
    private Stop FrederickTrainStop;
    private Trains trains;
    private ExponentialDistribution arrivalTimeRNG;

    // Fields used while running simulation
    private double currentTime;
    private boolean isFinished;
    private Queue<Person> finishedPeople;
    private int commuterCount; // the number of people who have been generated so far
    private double timeUntilNextArrival;
    private double ARRIVALTIMELAMBDA;

    // Constructor
    public Simulation(
            int numberOfBuses, int numberOfTrains,
            double distanceBetweenBusStops,
            double busSpeed, int busCapacity,
            double trainSpeed, int trainCapacity,
            double maxTimeOnBus, double maxTimeWaitingForBus,
            String citiesCSV,
            String trainStopsCSV, // todo: import train stop locations
            double arrivalTimeLambda
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
        TRAINSPEED = trainSpeed;
        MAXTIMEONBUS = maxTimeOnBus;
        MAXTIMEWAITINGFORBUS = maxTimeWaitingForBus;
        ARRIVALTIMELAMBDA = arrivalTimeLambda;

        // Initialize objects
        System.out.println("Initializing sim objects");

        FrederickTrainStop = new Stop(45,35);
        System.out.println("Frederick train stop created at " + FrederickTrainStop);

        trains = new Trains(numberOfTrains, trainSpeed, trainCapacity, FrederickTrainStop);
        System.out.println("Trains created:\n" + trains);

        fredrickCities = new Cities(citiesCSV, FrederickTrainStop, DISTANCEBETWEENBUSSTOPS, numberOfBuses, busSpeed, busCapacity);
        System.out.println("Frederick cities created: ");
        System.out.println(fredrickCities.printCities());
        NUMBEROFPEOPLE = fredrickCities.getTotalPopulation(); // have to set this after cities are loaded
        arrivalTimeRNG = new ExponentialDistribution(ARRIVALTIMELAMBDA);

        // Initialize tracking fields
        currentTime = 0;
        isFinished = false;
        finishedPeople = new Queue<Person>();
        setTimeUntilNextArrival();
        System.out.println("setting time until first arrival to " + timeUntilNextArrival);
        commuterCount = 0;
    }

    // Accessors
    public double getArrivalLambda() {
        return ARRIVALTIMELAMBDA;
    }
    public double getTimeUntilNextArrival() {
        return timeUntilNextArrival;
    }
    public Trains getTrains() {
        return trains;
    }

    // Mutators
    public void setArrivalLambda(double l) {
        ARRIVALTIMELAMBDA = l;
    }

    // Public Methods

    public void run() {
        System.out.println("Running simulation");
        double dt = timeUntilNextArrival; // set first dt to pass into update
        for (int i = 0; i < 10; i++) {
            dt = update(currentTime, dt);
        }
        // todo: export people and vmt data
    }

    // Advance the simulation by dt, and return the time until next event after that
    private double update(double currentTime, double dt) {
        System.out.println("Simulation Update Loop\n" +
                "Current time: " + currentTime +
                "\ndt for this loop: " + dt +
                "\nTime until next arrival: " + timeUntilNextArrival);
        this.currentTime += dt;
        System.out.println("after adding, current time is: " + currentTime);
        timeUntilNextArrival = Math.max(0, timeUntilNextArrival - dt);

        // add commuters to the simulation
        if (timeUntilNextArrival == 0) {
            fredrickCities.generateCommuter();
            commuterCount++;
            this.setTimeUntilNextArrival();
        }

        // update objects and determine the time of the next event in the simulation
        System.out.println("time until next arrival: " + timeUntilNextArrival);
        double fcitiesTime = fredrickCities.update(currentTime, dt);
        System.out.println("cities.update: " + fcitiesTime);
        double trainsTime = trains.update(currentTime, dt);
        System.out.println("trains.update: " + trainsTime);
        double timeUntilNextEvent = Math.min(Math.min(
                timeUntilNextArrival,
                fcitiesTime),
                trainsTime
        );

        // check if simulation is finished
        if (finishedPeople.getLength() == NUMBEROFPEOPLE) {
            isFinished = true;
        }
        System.out.println("finished update loop, time until next event is " + timeUntilNextEvent);
        System.out.println("finished: " + isFinished);
        return timeUntilNextEvent;
    }

    // Private Methods
    private void setTimeUntilNextArrival() {
        System.out.println("current commuter count in sim is " + this.commuterCount);
        // the rate of arrivals gradually increases until halfway through, then it decreases again
        if (commuterCount <= NUMBEROFPEOPLE/2) {
            setArrivalLambda(getArrivalLambda()*1.0007);
        } else {
            setArrivalLambda(getArrivalLambda()*0.9993);
        }
        this.timeUntilNextArrival = arrivalTimeRNG.sample(ARRIVALTIMELAMBDA);
        System.out.println("next arrival will be in " + this.timeUntilNextArrival);

    }

    public static void doArrivalUnitTests() {
        // testCities.csv contains:
        // name  x  y  pop   radius
        // test, 0, 0, 5000, 10
        System.out.println("Running simulation arrival unit tests");
        Simulation testSim = new Simulation(0,0,1,1,1,1,1,1,1,"testCities.csv", "testTrain.csv",2.0);
        System.out.println("Test sim number of people is " + testSim.NUMBEROFPEOPLE);
        try {
            File file = new File("Arrival_Results.csv");
            FileWriter writer = new FileWriter(file);

            for (int i = 0; i < testSim.NUMBEROFPEOPLE; i++) {
                if (i%100 == 0) {
                    writer.write("\n");
                    System.out.println("Test sim lambda is " + testSim.getArrivalLambda());
                }
                writer.write(testSim.getTimeUntilNextArrival() + ",");
                testSim.update(0,testSim.getTimeUntilNextArrival());
            }
            System.out.println("File created at " + file.getAbsolutePath());
            printLineAverages(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printLineAverages(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int lineNum = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(",");
                double sum = 0;
                int count = 0;

                for (String token : tokens) {
                    try {
                        sum += Double.parseDouble(token.trim());
                        count++;
                    } catch (NumberFormatException e) {
                        System.out.println("Non-numeric entry on line " + lineNum + ": " + token);
                    }
                }

                if (count > 0) {
                    double average = sum / count;
                    System.out.printf("Line %d average: %.4f%n", lineNum, average);
                } else {
                    System.out.println("Line " + lineNum + " contains no valid numbers.");
                }

                lineNum++;
            }
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }
}
