import java.util.ArrayList;

public class BusStops {

    // Private fields
    private ArrayList<BusStop> busStops;

    // Constructor
    public BusStops(double radius, double distance) {
        busStops = new ArrayList<BusStop>();
        generateBusStops(radius, distance);
    }

    // Accessors
    public ArrayList<BusStop> getStops() {
        return busStops;
    }

    // Public Methods



    // Private methods
    private void generateBusStops(double radius, double distance) {
        for (double x = -radius; x <= radius; x += distance) {
            for (double y = -radius; y <= radius; y += distance) {
                if (x * x + y * y <= radius * radius) {
                    busStops.add(new BusStop(x, y));
                }
            }
        }
    }
}
