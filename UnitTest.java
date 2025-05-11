import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class UnitTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running Unit Tests");

        // Location.doUnitTests();
        // Person.doUnitTests();
        // People.doUnitTests();
        // Vehicle.doUnitTests();
        // Bus.doUnitTests();
        // Cities.doUnitTests();
        // Simulation.doArrivalUnitTests();
        ExponentialDistribution.doUnitTests();
        analyze("Exponential RNG Results.csv");

    }

    public static void analyze(String filename) {
        int[] counts = new int[42]; // 0–40 plus index 41 for "higher"

        File file = new File(filename);
        System.out.println(file.getAbsolutePath());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextDouble()) {
                int number = (int) Math.round(scanner.nextDouble());
                if (number >= 0 && number <= 40) {
                    counts[number]++;
                } else {
                    counts[41]++;
                    System.out.println("Higher value: " + number);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i <= 40; i++) {
            System.out.println(i + ": " + counts[i]);
        }
        System.out.println("higher: " + counts[41]);

    }
}