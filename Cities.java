import java.io.File;
import java.text.ParseException;
import java.util.Scanner;

public class Cities {

    // Private Fields
    City[] cities;

    // Constructor
    public Cities(int count, String filename) throws Exception {
        cities = new City[count];
        if (!(importFromCSV(filename, cities))) {
            System.out.println("Error while importing city data");
        }
    }

    // Private Methods
    private boolean importFromCSV(String filename, City[] cities) {
        File file = new File( filename );
        Scanner scanner = null;
        try {
            scanner = new Scanner( file );
            int i = 0;
            while( scanner.hasNextLine() ) {
                String data = scanner.nextLine();
                cities[i++] = makeCity(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

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
        }
        return new City(name, x, y, population, distance, radius);
    }



    // Unit Testing Method

    public static void doUnitTests() {
        // todo: add unit tests
    }

}
