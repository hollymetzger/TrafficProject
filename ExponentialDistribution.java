import java.io.File;
import java.io.FileWriter;
import java.io.IOException; // Import file classes for the unit testing

public class ExponentialDistribution extends RandomDistribution {

    // Private fields
    private double lambda;

    // Constructor
    public ExponentialDistribution(double lamb) {
        lambda = lamb;
    }

    // Public Methods
    public double sample() {
        return Math.log(Math.random())/(-lambda);
    }

    // Testing Method
    public static void doUnitTests() {
        System.out.println("Running Exponential Distribution tests");
        ExponentialDistribution rng = new ExponentialDistribution(1.0);
        try {
            File file = new File("Exponential RNG Results.csv");
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < 1000; i++) {
                writer.write(rng.sample() + "\n");
            }
            System.out.println("File created at " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}