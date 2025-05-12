import java.util.ArrayList;

public class BusStops {

    // Private fields
    private ArrayList<Stop> busStops;
    private Stop train;

    // Constructor
    public BusStops(double radius, double distance, Stop t) {
        busStops = new ArrayList<Stop>();
        train = t;
        generateBusStops(radius, distance);
    }

    // Accessors
    public ArrayList<Stop> getStops() {
        return busStops;
    }
    public Stop getTrain() {
        return train;
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
