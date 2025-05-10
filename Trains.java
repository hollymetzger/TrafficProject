import java.io.File;
import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Trains {

    // Private fields
    private double timeBetweenTrains;
    private Train[] trains;
    private EnumMap<TrainStop, Stop> stops;

    // Constructor
    public Trains(int trainCount, double time, double speed, int capacity, Stop fTrain) {
        trains = new Train[trainCount];
        timeBetweenTrains = time;

        // Train stops are hard coded for simplicity
        stops = new EnumMap<>(TrainStop.class);
        stops.put(TrainStop.FREDERICK, fTrain); // Frederick train station (45,35)
        stops.put(TrainStop.GAITHERSBURG, new Stop(45,65, true)); // 30 miles from Frederick to Gaitherburg
        stops.put(TrainStop.ROCKVILLE, new Stop(45, 68, true)); // 3 miles from Gaithserburg to Rockville
        stops.put(TrainStop.BETHESDA, new Stop(45, 76, true)); // 8 miles from Rockville to Bethesda
        stops.put(TrainStop.WASHINGTON_DC, new Stop(45, 84, true)); // 8 miles from Bethesda to DC Metro Center

        for (int i = 0; i < trainCount; i++) {
            trains[i] = new Train(speed, capacity, stops);
        }
    }

    // Accessors
    public String toString() {
        return "Time between trains: " + timeBetweenTrains + "\n" +
                "Train array of length " + trains.length + "\n" +
                stops.size() + " Stations: ";
    }

    // public methods
    public double update(double currentTime, double dt) {
        double timeUntilNextEvent = Double.POSITIVE_INFINITY;
        for (Train train : trains) {
            timeUntilNextEvent = Math.min(train.update(currentTime, dt), timeUntilNextEvent);
        }
        return timeUntilNextEvent;
    }


    // private methods

    // Train station locations are imported from CSV
    public boolean importFromCSV(String filename, Stop[] stops) {
        System.out.println("Importing train data");
        File file = new File(filename);
        System.out.println(file.getAbsolutePath());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            int i = 0;
            while( scanner.hasNextLine() ) {
                String data = scanner.nextLine();
                String[] fields = data.split(",");
                String name = fields[0];
                double x = Double.parseDouble(fields[1]); // todo: break into makeTrainStop method?
                double y = Double.parseDouble(fields[2]);
                stops[i] = new Stop(x,y);
                i++;
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
        return true;
    }
}
