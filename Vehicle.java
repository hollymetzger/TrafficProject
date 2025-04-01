public class Vehicle {

    // private fields
    protected double speed;
    protected int maxCapacity;
    protected int currentCapacity;
    protected Person[] passengers;

    // Constructor
    public Vehicle(double speed, int maxCapacity) {
        this.speed = speed;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        this.passengers = new Person[maxCapacity];
    }

    // Accessors
    public double getSpeed() {
        return speed;
    }
    public int getCurrentCapacity() {
        return currentCapacity;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public String toString() {
        return "Speed: " + speed +
               "Capacity: " + currentCapacity + "/'" + maxCapacity;
    }

    public void pickUp(Person[] pass) {
        // add passengers from argument to this.passengers, taking the ones who have been waiting longest if
        // there are more than can fit

        // todo: make it so the vehicle takes the first arrived passengers and leaves if/once full
        for (int i = 0; i < pass.length; i++) {
            this.passengers[currentCapacity++] = pass[i];
        }
    }

    public void dropOff(Location destination) {
        // for person in passengers:
        //      if destination is their destination:
        //          get off vehicle
    }
}
