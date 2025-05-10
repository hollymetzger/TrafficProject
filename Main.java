import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args) {

        Simulation[] sims = new Simulation[1000];

        File file = new File("SimParameters.csv");
        System.out.println("Reading from: " + file.getAbsolutePath());

        try (Scanner scanner = new Scanner(file)) {
            int simNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");

                if (fields.length < 6) {
                    System.out.println("Skipping line " + simNumber + ": not enough fields.");
                    continue;
                }

                try {
                    int numberOfBuses = Integer.parseInt(fields[0]);
                    int numberOfTrains = Integer.parseInt(fields[1]);
                    double distanceBetweenBusStops = Double.parseDouble(fields[2]);
                    double timeBetweenTrains = Double.parseDouble(fields[3]);
                    int trainSpeed = Integer.parseInt(fields[4]);
                    int trainCapacity = Integer.parseInt(fields[5]);
                    double maxTimeOnBus = Double.parseDouble(fields[6]);
                    double maxTimeWaitingForBus = Double.parseDouble(fields[7]);

                    Simulation sim = new Simulation(
                            numberOfBuses, numberOfTrains,
                            distanceBetweenBusStops, timeBetweenTrains, trainSpeed, trainCapacity,
                            maxTimeOnBus, maxTimeWaitingForBus,
                            "cities.csv", "trainStops.csv"
                    );

                    sims[simNumber] = sim;


                } catch (NumberFormatException e) {
                    System.out.println("Error parsing numbers on line " + simNumber);
                    e.printStackTrace();
                }

                simNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Simulation parameter file not found.");
            e.printStackTrace();
        }

        // Run all simulations
        Simulation currentSim = sims[0];
        while (currentSim != null) {
            currentSim.run();
            // todo: run method should export strings of data, need to handle that
        }


    } // end of main method
}