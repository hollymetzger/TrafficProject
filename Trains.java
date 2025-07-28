/*

The class which imports and handles trains and train stops

 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Trains {

    // Private fields
    private int trainCount;
    private double timeBetweenTrains;
    private double timeUntilNextTrainLeaves;
    private double trainSpeed;
    private int trainCapacity;
    private ArrayList<Train> trains;
    private ArrayList<Train> finishedTrains;
    private Stop[] stops;

    // Constructor
    public Trains(int tc, double speed, int capacity, Stop ftrain) {
        trainCount = tc;
        trainSpeed = speed;
        trainCapacity = capacity;
        timeBetweenTrains = 80.0/trainCount;
        timeUntilNextTrainLeaves = timeBetweenTrains;
        trains = new ArrayList<Train>();
        finishedTrains = new ArrayList<Train>();
        importFromCSV("TrainStops.csv");
    }

    // Accessors
    public Stop[] getStops() { return stops; }
    public String toString() {
        return "Number of trains: " + trainCount + "\n" +
               "Time between trains: " + timeBetweenTrains + "\n" +
                "Time until next train leaves: " + timeUntilNextTrainLeaves + "\n" +
                "Train speed: " + trainSpeed + "\n" +
                "Train max capacity: " + trainCapacity + "\n" +
                stops.length + " Stations";
    }
    public void printTrainsStatus() {
        for (Train train : trains) {
            System.out.println(train.toString());
        }
    }

    // public methods
    public double update(double currentTime, double dt, Queue<Person> finishedPeople) {
        double timeUntilNextEvent = Double.POSITIVE_INFINITY;

        timeUntilNextTrainLeaves -= dt;
        if (timeUntilNextTrainLeaves <= 0) {
            System.out.println("Adding train to sim with speed " + trainSpeed);
            Train newTrain = new Train(trainSpeed, trainCapacity, stops);
            trains.add(newTrain);
            timeUntilNextTrainLeaves = timeBetweenTrains;
        }

        // move trains along track, pickup/dropoff as needed
        // when trains get to dc, after dropping off passengers, they are moved to the finished trains list
        for (int i = 0; i < trains.size(); i++) {
            System.out.println("Train #" + i);
            timeUntilNextEvent = Math.min(trains.get(i).update(currentTime, dt, finishedPeople), timeUntilNextEvent);
            System.out.println("now time until next train event is " + timeUntilNextEvent);
            if (trains.get(i).getNextStop().isEqual(stops[stops.length-1])) {
                finishedTrains.add(trains.remove(i));
            }
        }


        // System.out.println("Time until next train event: " + timeUntilNextEvent);
        return timeUntilNextEvent;
    }


    // private methods

    // Train station locations are imported from CSV
    public boolean importFromCSV(String filename) {
        // System.out.println("Importing train data");
        File file = new File(filename);
        // System.out.println(file.getAbsolutePath());
        Scanner scanner = null;
        ArrayList<Stop> alStops = new ArrayList<Stop>();
        try {
            scanner = new Scanner(file);
            while( scanner.hasNextLine() ) {
                alStops.add(makeTrainStop(scanner.nextLine()));
            }
        }  catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            System.out.println("Scanner was closed somehow");
            e.printStackTrace();
            return false;
        } catch (NoSuchElementException e) {
            System.out.println("Scanner tried to access line beyond EOF");
            e.printStackTrace();
            return false;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        Stop[] arStops = new Stop[alStops.size()];
        arStops = alStops.toArray(arStops);
        this.stops = arStops;
        return true;
    }

    private Stop makeTrainStop(String data) {
        String[] fields = data.split(",");
        String name = fields[0];
        double x = Double.parseDouble(fields[1]);
        double y = Double.parseDouble(fields[2]);
        return new Stop(x, y, true, name);
    }


    // Testing methods

    public static void testImport() {
        Trains testtrains = new Trains(5,5,5, new Stop(-1,-1,true,"placeholder"));
        System.out.println(testtrains.getStops().length + " stops");
        for (Stop s : testtrains.getStops()) {
            System.out.println(s);
        }
    }


    public static void doUnitTests() {
        Simulation testSim = new Simulation(
                1,
                10,
                1,1,
                1,
                1.25,
                222,
                1,
                1,
                "testCities.csv",
                "testTrain.csv",
                1.0
        );
        Queue<Person> fin = new Queue<Person>();

        Trains testTrains = testSim.getTrains();
        // System.out.println(testTrains.toString());
        testTrains.update(0.0, 8.0, fin);
        testTrains.printTrainsStatus();
        testTrains.update(8.0, 1.0, fin);
        testTrains.printTrainsStatus();
    }
}
