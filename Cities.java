import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;

public class Cities {

    // Private Fields
    private static ArrayList<City> cities;
    private static Stop train; // refers to the train station in Frederick

    // Constructor
    public Cities(String filename, Stop tr, double distance, int busCount, double busSpeed, int busCapacity) {
        cities = new ArrayList<City>();
        train = tr;
        if (!(importFromCSV(filename, distance))) {
            System.out.println("Error while importing city data");
        }

        // assign buses to each city per population
        int totalPop = getTotalPopulation();
        for (City city : cities) {
            int buses = busCount * city.getPopulation() / totalPop;
            city.initBuses(buses, busSpeed, busCapacity);
        }
    }

    // Accessors
    public int getLength() {
        return cities.size();
    }

    // Public Methods
    public double update(double currentTime, double dt) {
        double timeOfNextEvent = Double.POSITIVE_INFINITY;
        for (City city : cities) {
            timeOfNextEvent = Math.min(city.update(currentTime, dt), timeOfNextEvent);
        }
        return timeOfNextEvent;
    }

    public int getTotalPopulation() {
        int total = 0;
        for (City city : cities) {
            total += city.getPopulation();
        }
        return total;
    }

    public void generateCommuter() {
        int randomInt = (int) (Math.random()*getTotalPopulation());
        int populationSum = 0;

        // add up the cities' populations, and once we are over the random int, generate in that city
        for (City city : cities) {
            populationSum += city.getPopulation();
            if (randomInt <= populationSum) {
                city.generateCommuter();
            }
        }
    }

    public boolean importFromCSV(String filename, double distance) {
        File file = new File(filename);
        System.out.println(file.getAbsolutePath());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            int i = 0;
            while( scanner.hasNextLine() ) {
                String data = scanner.nextLine();
                cities.add(makeCity(data, distance));
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

    // Private Methods
    private static City makeCity(String data, double distance) {
        String name = null;
        double x = 0;
        double y = 0;
        int population = 0;
        double radius = 0;
        try {
            String[] fields = data.split(",");
            name = fields[0];
            x = Double.parseDouble(fields[1]);
            y = Double.parseDouble(fields[2]);
            population = Integer.parseInt(fields[3]);
            radius = Double.parseDouble(fields[4]);
        } catch (NumberFormatException nfe) {
            System.out.println("Unable to parse number(s) from this city data: ");
            System.out.println(data);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Insufficient number of fields in CSV file");
        }
        return new City(name, x, y, population, distance, radius, 10, train); // todo: add bus count variable
    }



    // Unit Testing Method
    public static void doUnitTests() throws Exception {
        Stop train = new Stop(1.0,1.0);
        Cities citiesTest = new Cities("src/cities.csv", train, 1.0, 100, 25, 20);
        for (int i = 0; i < citiesTest.getLength(); i++) {
            System.out.println(cities.get(i));
        }
    }
}
