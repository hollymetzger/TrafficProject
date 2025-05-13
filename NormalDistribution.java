import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NormalDistribution extends RandomDistribution {
    private double mu, sigma;

    public NormalDistribution(double m, double s) {
        mu = m;
        sigma = s;
    }

    public double sample() {
        double sum = 0;
        for(int i = 0; i < 30; i++) {
            sum += Math.random();
        }
        return ((sum - 15)/Math.sqrt(30.0/12)) * sigma + mu;
    }

    public double sample(double m) {
        double sum = 0;
        for(int i = 0; i < 30; i++) {
            sum += Math.random();
        }
        return ((sum - 15)/Math.sqrt(30.0/12)) * sigma + m;
    }

    public static void doUnitTests() {
        System.out.println("Running Normal Distribution tests");
        NormalDistribution rng = new NormalDistribution(1.0, 5.0);
        try {
            File file = new File("Normal RNG Results.csv");
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < 500; i++) {
                Double ran = rng.sample(34.0);
                System.out.format("%.3f",ran);
                System.out.print(",");
            }
            System.out.println("File created at " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}