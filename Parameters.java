import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parameters {
    public static double MAXTIMEONBUS;
    public static Location METROLOCATION;
    public static int NUMBEROFBUSES;
    public static int NUMBEROFTRAINS;
    public static double BUSSPEED;

    public Parameters(String filename) {
        File file = new File(filename);
        System.out.println(file.getAbsolutePath());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] fields = data.split(",");
                MAXTIMEONBUS = Double.parseDouble(fields[0]);
                double metroX = Double.parseDouble(fields[1]);
                double metroY = Double.parseDouble(fields[2]);
                METROLOCATION = new Location(metroX, metroY);
                NUMBEROFBUSES = Integer.parseInt(fields[3]);
                NUMBEROFTRAINS = Integer.parseInt(fields[4]);
                BUSSPEED = Double.parseDouble(fields[5]);
            } else {
                System.out.println("Parameter file is empty.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Unable to parse parameter values from file:");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough fields in parameter file.");
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}