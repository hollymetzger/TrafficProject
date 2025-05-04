public class Train extends Vehicle {

    // Private fields
    private double distanceToNextStop;
    private double totalDistanceTraveled;

    // Constructor
    public Train(double speed, int maxCapacity) {
        super(speed, maxCapacity);
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
        }

        // update passengers on bus
        for (int i = 0; i < currentCapacity; i++) {
            Person person = passengers[i];
            person.update(currentTime, dt, nextStop);
        }

        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        return distanceToNextStop/speed; // return the time it will take to reach next stop
    }

    private void setNextStop(BusStops stops) {


    }

}
