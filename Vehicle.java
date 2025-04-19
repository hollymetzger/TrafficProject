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

    public void pickUp(BusStop stop) {
        // add passengers from Bus Stop to this.passengers, taking the ones who have been waiting longest if
        // there are more than can fit

        // todo: add passengers from stop's queue to bus, until bus is full


    }

    public void dropOff(Location destination) {
        // todo: implement this
        // for person in passengers:
        //      if destination is their destination:
        //          get off vehicle
    }

    public static void doUnitTests() {
        System.out.println("Running Vehicle Tests");

        int testCount = 0;
        int failCount = 0;

        Vehicle v = new Vehicle(25, 10);
        Person[] people = new Person[5];
        for (int i=0; i < 5; i++) {
            people[i] = new Person(new Location(i*1.0,1*1.0), new Location(i*2.0,1*2.0));
        }
        v.pickUp(people);

        if (v.currentCapacity != 5) {
            System.out.println(("Fail: current capacity should be 5"));
            failCount++;
        }
        testCount++;

        System.out.printf("Vehicle tests passed: %d/%d\n",testCount-failCount, testCount);
    }
}
