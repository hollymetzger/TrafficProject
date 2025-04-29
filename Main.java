import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args) {
        File file = new File("SimParameters.csv");
        System.out.println("Reading from: " + file.getAbsolutePath());

        try (Scanner scanner = new Scanner(file)) {
            int runNumber = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");

                if (fields.length < 7) {
                    System.out.println("Skipping line " + runNumber + ": not enough fields.");
                    continue;
                }

                try {
                    int numberOfBuses = Integer.parseInt(fields[0]);
                    int numberOfTrains = Integer.parseInt(fields[1]);
                    double timeBetweenTrains = Double.parseDouble(fields[2]);
                    int trainSpeed = Integer.parseInt(fields[3]);
                    double maxTimeOnBus = Double.parseDouble(fields[4]);
                    double maxTimeWaitingForBus = Double.parseDouble(fields[5]);
                    String citiesCSV = fields[6];

                    Simulation sim = new Simulation(
                            numberOfBuses, numberOfTrains,
                            timeBetweenTrains, trainSpeed,
                            maxTimeOnBus, maxTimeWaitingForBus,
                            citiesCSV
                    );


                } catch (NumberFormatException e) {
                    System.out.println("Error parsing numbers on line " + runNumber);
                    e.printStackTrace();
                }

                runNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Simulation parameter file not found.");
            e.printStackTrace();
        }
    }
}