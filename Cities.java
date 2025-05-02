import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;

public class Cities {

    // Private Fields
    static ArrayList<City> cities;

    // Constructor
    public Cities(String filename) {
        cities = new ArrayList<City>();
        if (!(importFromCSV(filename))) {
            System.out.println("Error while importing city data");
        }
    }

    // Accessors
    public int getLength() {
        return cities.size();
    }

    // Public Methods

    public double update(double currentTime, double dt) {
        return -1.0;
    }

    public boolean importFromCSV(String filename) {
        File file = new File(filename);
        System.out.println(file.getAbsolutePath());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            int i = 0;
            while( scanner.hasNextLine() ) {
                String data = scanner.nextLine();
                cities.add(makeCity(data));
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

    public int getTotalPopulation() {
        int total = 0;
        for (City city : cities) {
            total += city.getPopulation();
        }
        return total;
    }

    // Private Methods
    private static City makeCity(String data) {
        String name = null;
        double x = 0;
        double y = 0;
        int population = 0;
        double distance = 0;
        double radius = 0;
        try {
            String[] fields = data.split(",");
            name = fields[0];
            x = Double.parseDouble(fields[1]);
            y = Double.parseDouble(fields[2]);
            population = Integer.parseInt(fields[3]);
            radius = Double.parseDouble(fields[4]);
            distance = Double.parseDouble(fields[5]);
        } catch (NumberFormatException nfe) {
            System.out.println("Unable to parse number(s) from this city data: ");
            System.out.println(data);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Insufficient number of fields in CSV file");
        }
        return new City(name, x, y, population, distance, radius, 10); // todo: add bus count variable
    }



    // Unit Testing Method
    public static void doUnitTests() throws Exception {
        Cities citiesTest = new Cities("src/cities.csv");
        for (int i = 0; i < citiesTest.getLength(); i++) {
            System.out.println(cities.get(i));
        }
    }
}
