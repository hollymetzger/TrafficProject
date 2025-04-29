public class City extends Location {
    private String name;
    private int population;
    private double radius, distance;
    private Bus[] buses;
    private BusStops busStops;
    private double timeUntilNextArrival;
    private ExponentialDistribution distanceRNG;

    // Constructor
    public City (String nm, double x, double y, int pop, double r, double dis) {
        super(x, y);
        name = nm;
        population = pop;
        radius = r;
        distance = dis;
        busStops = new BusStops(radius, distance);
        timeUntilNextArrival = Double.POSITIVE_INFINITY;

    }

    // Accessors
    public int getPopulation() {
        return population;
    }
    public String toString() {
        return "Name: " + name +
                "\nPopulation: " + population +
                "\nRadius: " + radius +
                "\nDistance between bus stops: " + distance;
    }

    // Public Methods

    public double update(double currentTime, double dt) {
        timeUntilNextArrival = Math.max (0, timeUntilNextArrival-=dt);
        if (timeUntilNextArrival == 0) {
            generate();
        }
        return timeUntilNextArrival;
    }

    public void generate() {
        // generate random point
        double theta = Math.TAU*Math.random();
        double r = distanceRNG.sample();
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);

        // Create Person object
        Location home = new Location(x,y);
        Location destination = new Location(x,y); // todo: generate end location same way
        Person person = new Person(home, destination);

        // Add the person to the queue of the nearest bus stop in the city
        BusStop stop = home.getNearest(busStops.getStops());

        for (BusStop s : this.busStops.getStops()) {
            if (s.isEqual(stop)) {
                s.add(person);
                return;
            }
        }
        System.out.println("Error! Didn't add person to any bus stop!!");
    }

    // Private Methods
    private double setNextArrivalTime(double currentTime) {
        // todo: exponentially determine next arriival time, where average time
        //  between arrivals decreases as time goes on, then goes back up toward the end
        return arrivalTimeRNG.sample();
    }


    // Unit Testing Method

    public static void doUnitTests() {
        // todo: add unit tests
    }
}
