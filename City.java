public class City extends Location {
    private String name;
    private int population;
    private double radius, distance;
    private BusStops busStops;

    // Constructor
    public City (String nm, double x, double y, int pop, double r, double dis) {
        super(x, y);
        name = nm;
        population = pop;
        radius = r;
        distance = dis;
        busStops = new BusStops(radius, distance);
    }

    // Public Methods


    // Private Methods



    // Unit Testing Method

    public static void doUnitTests() {
        // todo: add unit tests
    }
}
