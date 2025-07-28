public class City extends Location {
    private String name;

    // todo change to residents for clarity?
    private int population; // number of people who live in this city
    private int workers; // number of people who commute into this city. not used for exact tracking, only to proportionally assign
    private double radius, distance;
    private Bus[] buses;
    private BusStops busStops;
    private ExponentialDistribution distanceRNG;

    // Constructor
    public City (String nm, double x, double y, int pop, int w, double r, double dis, int busCount, Stop train) {
        super(x, y);
        name = nm;
        population = pop;
        workers = w;
        radius = r;
        distance = dis;
        busStops = new BusStops(radius, distance, train, this);
        distanceRNG = new ExponentialDistribution(setLambda(radius));
    }

    // Accessors
    public int getPopulation() {
        return population;
    }
    public int getWorkers() { return workers; }
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
        double angleBetweenBuses = Math.TAU/buses.length;
        for (int i = 0; i < buses.length; i++) {
            double theta = angleBetweenBuses*i;
            double r = this.radius/3;
            Location cartesian = convertPolar(theta, r);
            cartesian.addVector(this);
            Stop busStartStop = cartesian.getNearest(busStops.getStops());
            buses[i] = new Bus(speed, capacity, busStartStop, busStops.getTrain());
            System.out.println("Placing bus at " + busStartStop);
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

    public boolean generateCommuter() {
        // double check not to spawn extra people
        if (population == 0) {
            System.out.println("tried to spawn person that doesn't exist");
            return false;
        }

        // generate random point
        double theta = Math.TAU*Math.random();
        double r = Math.min(distanceRNG.sample(), this.radius);

        // Create Person object
        Location home = convertPolar(theta, r);
        home.addVector(this);
        Location destination = home; // todo: generate end location same way
        Person person = new Person(home, destination, getName());
        population--;

        // Add the person to the queue of the nearest bus stop in the city
        home.getNearest(busStops.getStops()).add(person, true);
        System.out.println("Generating commuter in " + getName() +
                "\nAdding to bus stop at " + home.getNearest(busStops.getStops()).toString());
        return true;
    }

    private double setLambda(double radius) {
        return 0.929 * Math.exp(-0.057 * radius);
    }

    private Location convertPolar(double theta, double r) {
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        return new Location(x,y);
    }

    // Unit Testing Method
    public static void doUnitTests() {
        // todo: add unit tests
    }
}
