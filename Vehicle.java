public class Vehicle {

    // private fields
    protected double speed;
    protected int maxCapacity;
    protected int currentCapacity;
    protected Queue<Person> passengers;

    // Constructor
    public Vehicle(double speed, int maxCapacity) {
        this.speed = speed;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        this.passengers = new Queue<Person>();
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

    // add passengers from queue to this.passengers
    public void pickUp(Queue<Person> people) {
        System.out.println(this.getClass() + " Picking up passengers");
        Person pass = people.dequeue();
        while (currentCapacity < maxCapacity && pass != null) {
            passengers.enqueue(pass);
            pass = people.dequeue();
            currentCapacity++;
        }
    }

    // drops off all passengers on vehicle
    public void dropOff(Stop stop) {
        Person person = passengers.dequeue();
        while (person != null) {
            stop.add(person, false);
            person = passengers.dequeue();
        }

    }

    public static void doUnitTests() {
        System.out.println("Running Vehicle Tests");

        int testCount = 0;
        int failCount = 0;

        Vehicle v = new Vehicle(25, 10);
        Queue<Person> people = new Queue<Person>();
        for (int i=0; i < 5; i++) {
            people.enqueue(new Person(new Location(i*1.0,1*1.0), new Location(i*2.0,1*2.0), "home"));
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
