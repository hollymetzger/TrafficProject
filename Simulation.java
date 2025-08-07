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
    int numPeople, numBuses, numTrains;
    double busStopDistance;
    double busSpeed, trainSpeed;
    double maxTimeOnBus, maxTimeWaitingForBus;

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
    private double arrivalTimeLambda;

    // Constructor
    public Simulation(
            int numBuses, int numTrains,
            double busStopDistance,
            double busSpeed, int busCapacity,
            double trainSpeed, int trainCapacity,
            double maxTimeOnBus, double maxTimeWaitingForBus,
            String citiesCSV,
            String trainStopsCSV, // todo: import train stop locations
            double arrivalTimeLambda
    ) {
        System.out.println("Initializing Simulation with the following parameters:\n" +
                numBuses + " buses\n" +
                numTrains + " trains\n" +
                busStopDistance + " distance between bus stops\n" +
                "City data from " + citiesCSV);

        // Set parameters
        this.numBuses = numBuses;
        this.numTrains = numTrains;
        this.busStopDistance = busStopDistance;
        this.trainSpeed = trainSpeed;
        this.maxTimeOnBus = maxTimeOnBus;
        this.maxTimeWaitingForBus = maxTimeWaitingForBus;
        this.arrivalTimeLambda = arrivalTimeLambda;

        // Initialize objects
        System.out.println("Initializing sim objects");

        FrederickTrainStop = new Stop(45,35, true, "Frederick");
        System.out.println("Frederick train stop created at " + FrederickTrainStop);

        trains = new Trains(numTrains, trainSpeed, trainCapacity, FrederickTrainStop);
        System.out.println("Trains created:\n" + trains);

        fredrickCities = new Cities(citiesCSV, FrederickTrainStop, busStopDistance, numBuses, busSpeed, busCapacity);
        System.out.println("Frederick cities created: ");
        numPeople = fredrickCities.getTotalPopulation(); // have to set this after cities are loaded
        arrivalTimeRNG = new ExponentialDistribution(arrivalTimeLambda);

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
        return arrivalTimeLambda;
    }
    public double getTimeUntilNextArrival() {
        return timeUntilNextArrival;
    }
    public Trains getTrains() {
        return trains;
    }

    // Mutators
    public void setArrivalLambda(double l) {
        arrivalTimeLambda = l;
    }
    public void addTime(double dt) { this.currentTime += dt; }

    // Public Methods

    public void run() {
        System.out.println("Running simulation");
        double dt = timeUntilNextArrival; // set first dt to pass into update
        while(finishedPeople.getLength() < numPeople) {
            dt = update(currentTime, dt);
            // end run if something went wrong in update()
            if (dt == -1.0) {
                return;
            }
        }
        System.out.println("Finished people: " + finishedPeople.getLength());
        // todo: export people and vmt data
    }

    // Advance the simulation by dt, and return the time until next event after that
    private double update(double currentTime, double dt) {

                System.out.println("\n\n\nSimulation Update Loop\n" +
                "Current time: " + currentTime +
                "\ndt for this loop: " + dt +
                "\nTime until next arrival: " + timeUntilNextArrival);
        addTime(dt);
        timeUntilNextArrival = Math.max(0, timeUntilNextArrival - dt);

        // add commuters to the simulation
        if (timeUntilNextArrival == 0 && fredrickCities.generateCommuter()) {
            System.out.println("incing cc");
            commuterCount++;
            this.setTimeUntilNextArrival();
        }

        // update objects and determine the time of the next event in the simulation
        System.out.println("time until next arrival: " + timeUntilNextArrival);
        double fcitiesTime = fredrickCities.update(currentTime, dt);
        System.out.println("cities.update: " + fcitiesTime);
        double trainsTime = trains.update(currentTime, dt, finishedPeople);
        System.out.println("trains.update: " + trainsTime);
        double timeUntilNextEvent = Math.min(Math.min(
                timeUntilNextArrival,
                fcitiesTime),
                trainsTime
        );

        // check if simulation is finished
        if (finishedPeople.getLength() == numPeople) {
            isFinished = true;
        }
        System.out.println("finished update loop, time until next event is " + timeUntilNextEvent);
        System.out.println("finished people count: " + isFinished);

        // check if current time is NaN
        if (Double.isNaN(currentTime)) {
            System.out.println("Error! Current Time = NaN");
            return -1.0;
        }

        return timeUntilNextEvent;
    }

    // Private Methods
    private void setTimeUntilNextArrival() {
        System.out.println("current commuter count in sim is " + this.commuterCount);
        // the rate of arrivals gradually increases until halfway through, then it decreases again
        if (commuterCount <= numPeople/2) {
            setArrivalLambda(getArrivalLambda()*1.0007);
        } else if (commuterCount == numPeople) {
            System.out.println("no people remaining to generate");
            timeUntilNextArrival = Double.POSITIVE_INFINITY;
            return;
        }
        else {
            setArrivalLambda(getArrivalLambda()*0.9993);
        }
        this.timeUntilNextArrival = arrivalTimeRNG.sample(arrivalTimeLambda);
        System.out.println("next arrival will be in " + this.timeUntilNextArrival);

    }

    public static void doArrivalUnitTests() {
        // testCities.csv contains:
        // name  x  y  pop   radius
        // test, 0, 0, 5000, 10
        System.out.println("Running simulation arrival unit tests");
        Simulation testSim = new Simulation(0,0,1,1,1,1,1,1,1,"testCities.csv", "testTrain.csv",2.0);
        System.out.println("Test sim number of people is " + testSim.numPeople);
        try {
            File file = new File("Arrival_Results.csv");
            FileWriter writer = new FileWriter(file);

            for (int i = 0; i < testSim.numPeople; i++) {
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
