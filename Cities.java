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
        String[] fields = data.split(",");
        String name = fields[0];
        double x = Double.parseDouble(fields[1]);
        double y = Double.parseDouble(fields[2]);
        int population = Integer.parseInt(fields[3]);
        double radius = Double.parseDouble(fields[4]);
        double distance = Double.parseDouble(fields[5]);
        return new City(name, x, y, population, radius, distance);
    }

}
