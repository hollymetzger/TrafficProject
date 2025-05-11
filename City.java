public class City extends Location {
    private String name;
    private int population;
    private double radius, distance;
    private Bus[] buses;
    private BusStops busStops;
    private ExponentialDistribution distanceRNG;

    // Constructor
    public City (String nm, double x, double y, int pop, double r, double dis, int busCount, Stop train) {
        super(x, y);
        name = nm;
        population = pop;
        radius = r;
        distance = dis;
        busStops = new BusStops(radius, distance, train);
        distanceRNG = new ExponentialDistribution(setLambda(radius));
    }

    // Accessors
    public int getPopulation() {
        return population;
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return "Name: " + name +
                "\nPopulation: " + population +
                "\nRadius: " + radius +
                "\nNumber of buses: " + buses.length +
                "\nDistance between bus stops: " + distance;
    }

    // Mutators
    public void initBuses(int count, double speed, int capacity) {
        buses = new Bus[count];
        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus(speed, capacity, new Stop(this.getX(), this.getY()), busStops.getTrain());
        }
    }


    // Public Methods
    public double update(double currentTime, double dt) {
        System.out.println("Updating " + this.getName());
        double timeOfNextEvent = Double.POSITIVE_INFINITY;
        for (Bus bus : buses) {
            timeOfNextEvent = Math.min(bus.update(currentTime, dt, busStops), timeOfNextEvent);
        }
         return timeOfNextEvent;
    }

    public void generateCommuter() {
        if (population == 0) { return; } // double check not to spawn extra people

        // generate random point
        double theta = Math.TAU*Math.random();
        double r = Math.min(distanceRNG.sample(), this.radius);
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);

        // Create Person object
        Location home = new Location(x,y);
        Location destination = new Location(x,y); // todo: generate end location same way
        Person person = new Person(home, destination, getName());

        // Add the person to the queue of the nearest bus stop in the city
        home.getNearest(busStops.getStops()).add(person, true);
        System.out.println("Generating commuter in " + getName() +
                "\nAdding to bus stop at " + home.getNearest(busStops.getStops()).toString());
    }

    private double setLambda(double radius) {
        return 0.929 * Math.exp(-0.057 * radius);
    }

    // Unit Testing Method
    public static void doUnitTests() {
        // todo: add unit tests
    }
}
