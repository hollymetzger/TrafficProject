public class Bus extends Vehicle {
    private double timeToMetroStation;
    private double distanceToNextStop;
    private Location nextStop;
    private Location metro;

    // Constructor
    public Bus(double speed, int maxCapacity) {
        super(speed, maxCapacity);
    }

    // Accessors
    public double getTimeToMetroStation() {
        return timeToMetroStation;
    }
    public Location getNextStop() {
        return nextStop;
    }
    public String toString() {
        return super.toString() + "\n" +
               "Next stop: " + nextStop.toString();
    }

    // Public methods

    // Returns a Location the bus will travel to next
    public Location determineNextStop() {
        if (currentCapacity == maxCapacity) {
            return metro;
        }
        return new Location(0,0);
    }

    // Using the location determined above, calculate distance and set this.nextStop
    public void setNextStop(Location ns) {
        Location currentStop = this.nextStop;
        this.nextStop = ns;
        this.distanceToNextStop = currentStop.getDistance(ns);
    }

    // Advances the bus in the simulation by dt time
    public double update(double currentTime, double dt) {

        // if we have reached the stop, unload passengers and determine next stop
        if (distanceToNextStop == 0) {
            // todo: pickup and drop off passengers
            this.setNextStop(this.determineNextStop());
        }

        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        return distanceToNextStop/speed; // return the time it will take to reach next stop
    }

    // Unit testing method
    public static void doUnitTests() {
        System.out.println("Running Bus Tests");
    }
}
