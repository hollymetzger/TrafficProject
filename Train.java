public class Train extends Vehicle {

    // Private fields
    private double distanceToNextStop;
    private double totalDistanceTraveled;
    private Stop[] stops;
    private int nextStop;
    private boolean southbound;

    // Constructor
    public Train(double speed, int maxCapacity, Stop[] s) {
        super(speed, maxCapacity);
        stops = s;
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

        // if we have reached the stop, unload passengers and set next stop
        if (distanceToNextStop == 0) {
            // todo: pickup and drop off passengers, checking if this is their stop
            setNextStop();
        }

        // update passengers on train
        for (int i = 0; i < currentCapacity; i++) {
            Node<Person> person = passengers.getHead();
            while (person != null) {
                person.getData().update(currentTime,dt);
                person = person.getNext();
            }
        }

        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        return distanceToNextStop/speed; // return the time it will take to reach next stop
    }

    public void dropOff(Stop stop) {

    }


    private void setNextStop() {
        int tmp = nextStop;

        nextStop += isSouthbound() ? 1 : -1;

        // if we went out of bounds, reverse it
        if (nextStop == -1 || nextStop == stops.length) {
            reverseDirection();
            nextStop += isSouthbound() ? -2 : 2;
        }

        distanceToNextStop = stops[tmp].getDistance(stops[nextStop]);
    }

    private void reverseDirection() {
        southbound = !southbound;
    }
}
