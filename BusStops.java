import java.util.ArrayList;

public class BusStops {

    // Private fields
    private ArrayList<Location> busStops;

    // Constructor
    public BusStops(double radius, double distance) {
        busStops = new ArrayList<Location>();
        generateBusStops(radius, distance);
    }

    // Public methods

    // Private methods
    private void generateBusStops(double radius, double distance) {
        for (double x = -radius; x <= radius; x += distance) {
            for (double y = -radius; y <= radius; y += distance) {
                if (x * x + y * y <= radius * radius) {
                    busStops.add(new Location(x, y));
                }
            }
        }
    }
}
