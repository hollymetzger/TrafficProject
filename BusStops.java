import java.util.ArrayList;

public class BusStops {

    // Private fields
    private ArrayList<Stop> busStops;
    private Stop metro;

    // Constructor
    public BusStops(double radius, double distance, Stop metro) {
        busStops = new ArrayList<Stop>();
        generateBusStops(radius, distance);
    }

    // Accessors
    public ArrayList<Stop> getStops() {
        return busStops;
    }

    // Private methods
    private void generateBusStops(double radius, double distance) {
        for (double x = -radius; x <= radius; x += distance) {
            for (double y = -radius; y <= radius; y += distance) {
                if (x * x + y * y <= radius * radius) {
                    busStops.add(new Stop(x, y));
                }
            }
        }
    }
}
