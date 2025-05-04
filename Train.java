public class Train extends Vehicle {

    // Private fields
    private double distanceToNextStop;
    private double totalDistanceTraveled;
    private Stop[] stops;
    private int nextStop;
    private boolean southbound;

    // Constructor
    public Train(double speed, int maxCapacity) {
        super(speed, maxCapacity);
    }

    // Accessors
    public boolean isSouthbound() {
        return southbound;
    }

    // Public methods
    // Advances the train in the simulation by dt time
    public double update(double currentTime, double dt) {

        double distance = speed*dt;
        distanceToNextStop -= distance;
        totalDistanceTraveled += distance;

        // if we have reached the stop, unload passengers and determine next stop
        if (distanceToNextStop == 0) {
            // todo: pickup and drop off passengers
            setNextStop();

        }

        // update passengers on bus
        for (int i = 0; i < currentCapacity; i++) {
            Person person = passengers[i];
            person.update(currentTime, dt);
        }

        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        return distanceToNextStop/speed; // return the time it will take to reach next stop
    }

    private void setNextStop() {
        int tmp = nextStop;

        nextStop += southbound ? 1 : -1;
        if (nextStop == -1 || nextStop == stops.length) {
            reverseDirection();
            nextStop += southbound ? -2 : 2;
        }

        distanceToNextStop = stops[tmp].getDistance(stops[nextStop]);
    }

    private void reverseDirection() {
        southbound = !southbound;
    }
}
