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

        // convert destination cities worker count to proportion


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
    public String printCities() {
        StringBuilder citiesStr = new StringBuilder();
        for (City city : cities) {
            citiesStr.append(city.toString());
            citiesStr.append("\n");
        }
        return citiesStr.toString();
    }

    // Public Methods
    public double update(double currentTime, double dt) {
        System.out.println("Updating cities");
        double timeOfNextEvent = Double.POSITIVE_INFINITY;
        for (City city : cities) {
            double cityTime = city.update(currentTime, dt);
            System.out.println(city.getName() + " next event in " + cityTime);
            timeOfNextEvent = Math.min(cityTime, timeOfNextEvent);
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

    public boolean generateCommuter() {
        System.out.println("cities generating commuter");
        int randomInt = (int) (Math.random()*getTotalPopulation());
        int populationSum = 0;
        boolean commuterAdded = false;

        // add up the cities' populations, and once we are over the random int, generate in that city
        for (City city : cities) {
            populationSum += city.getPopulation();
            if (randomInt <= populationSum) {
                commuterAdded = city.generateCommuter();
            }
        }
        return commuterAdded;
    }

    public boolean importFromCSV(String filename, double distance) {
        System.out.println("running cities.import from csv");
        File file = new File(filename);
        System.out.println(file.getAbsolutePath());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                System.out.println("making city with data: " + data);
                City newCity = makeCity(data, distance);
                cities.add(newCity);
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

    /* public City getWeightedDestinationCity() {
        int randomInt = (int) (Math.random()*getTotalPopulation());
        // add up the cities' populations, and once we are over the random int, generate in that city
        for (City city : cities) {
            populationSum += city.getPopulation();
            if (randomInt <= populationSum) {
                commuterAdded = city.generateCommuter();
            }
        }
    } */

    // adds the total number of workers to proportionally assign destination cities to persons
    // since this isn't tracked and doesn't change, it is only called once in the constructor and stored statically
    private int sumWorkers() {
        int total = 0;
        for (City city : cities) {
            total += city.getWorkers();
        }
        return total;
    }

    // Private Methods
    private static City makeCity(String data, double distance) {
        String name = null;
        double x = 0;
        double y = 0;
        int population = 0;
        int workers = 0;
        double radius = 0;
        try {
            String[] fields = data.split(",");
            name = fields[0];
            x = Double.parseDouble(fields[1]);
            y = Double.parseDouble(fields[2]);
            population = Integer.parseInt(fields[3]);
            workers = Integer.parseInt(fields[4]);
            radius = Double.parseDouble(fields[5]);
        } catch (NumberFormatException nfe) {
            System.out.println("Unable to parse number(s) from this city data: ");
            System.out.println(data);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Insufficient number of fields in CSV file");
        } catch (Exception e) {
            System.out.println("Other Exception while parsing line: " + data);
            e.printStackTrace();
            return null;
        }
        return new City(name, x, y, population, workers, radius, distance, -1, train);
    }



    // Unit Testing Method
    public static void doUnitTests() throws Exception {
        Stop train = new Stop(1.0,1.0);
        Cities citiesTest = new Cities("src/cities.csv", train, 1.0, 100, 25, 20);

    }
}
