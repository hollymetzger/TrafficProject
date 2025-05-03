import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Trains {

    // Private fields
    private double timeBetweenTrains;
    private Train[] trains;
    private Stop[] stops;

    // Constructor
    public Trains(int trainCount, double time, String filename) {
        trains = new Train[trainCount];
        timeBetweenTrains = time;
        importFromCSV(filename);
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
    public boolean importFromCSV(String filename) {
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
                double x = Double.parseDouble(fields[1]);
                double y = Double.parseDouble(fields[2]);
                stops[i] = new Stop(x,y);
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
