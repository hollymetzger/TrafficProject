public class Simulation {
    // Parameters for Simulation
    double MAXTIMEONBUS;
    int NUMBEROFPEOPLE, NUMBEROFBUSES, NUMBEROFTRAINS;
    double TIMEBETWEENTRAINS;
    int DISTANCEBETWEENBUSSTOPS;
    int BUSSPEED, TRAINSPEED;


    // Arrays to hold objects
    private Bus[] buses;
    private Person[] people;

    // Fields used while running simulation
    private double currentTime = 0;

    // Constructor
    public Simulation() {
        people = new Person[Parameters.NUMBEROFPEOPLE];
        buses = new Bus[Parameters.NUMBEROFBUSES];
    }

    double update(double currentTime, double dt) {
        double timeUntilNextEvent = Double.POSITIVE_INFINITY;

        for (int i = -0; i < buses.length; i++) {
            double time = buses[i].update(currentTime, dt);
            if (time < timeUntilNextEvent) {
                timeUntilNextEvent = time;
            }
        }

        return timeUntilNextEvent;
    }
}
