import java.util.EnumMap;
import java.util.Map;

public class Train extends Vehicle {

    // Private fields
    private double distanceToNextStop;
    private double totalDistanceTraveled;
    private EnumMap<TrainStop, Stop> stops;
    private TrainStop nextStop;
    private boolean southbound;

    // Constructor
    public Train(double speed, int maxCapacity, EnumMap<TrainStop, Stop> stopsMap) {
        super(speed, maxCapacity);
        stops = stopsMap;
        nextStop = TrainStop.FREDERICK;
    }

    // Accessors
    public boolean isSouthbound() {
        return southbound;
    }
    public TrainStop getNextStop() {
        return nextStop;
    }
    public String toString() {
        return "Next stop: " + nextStop + "\n" +
               "Distance to next stop: " + distanceToNextStop + "\n" +
               "Total distance traveled: " + totalDistanceTraveled + "\n" +
               "Southbound: " + southbound;
    }

    // Public methods
    // Advances the train in the simulation by dt time
    public double update(double currentTime, double dt, Queue<Person> finishedPeople) {

        // if we have reached the stop, unload passengers and set next stop
        if (distanceToNextStop == 0) {
            // System.out.println("Train reached stop " + nextStop);
            pickUp(stops.get(nextStop).getLine());
            dropOff(stops.get(nextStop), finishedPeople);
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

        double distance = speed*dt;
        distanceToNextStop -= distance;
        totalDistanceTraveled += distance;

        distanceToNextStop = Math.max(distanceToNextStop - dt * speed, 0); // get closer to stop
        // System.out.println("time it will take to reach next stop: " + distanceToNextStop/speed);
        return distanceToNextStop/speed; // return the time it will take to reach next stop
    }

    public void dropOff(Stop stop, Queue<Person> finishedPeople) {
        Queue<Person> disembarking = removeDisembarkingPassengers();
        while (!disembarking.isEmpty()) {
            Person person = disembarking.dequeue();
            //  stop.getLine().enqueue(person); this should be the next line but for now i will
            // finish them here
            finishedPeople.enqueue(person);

        }
    }



    // Private methods

    // removes the Persons who should be disembarking at this.nextStop from this.passengers and returns them as a queue
    private Queue<Person> removeDisembarkingPassengers() {
        Queue<Person> disembarking = new Queue<Person>();
        Queue<Person> remaining = new Queue<Person>();

        // iterate through queue and sort each node into their respective queues
        Node<Person> passenger = passengers.getHead();
        while (passenger != null) {
            if (passenger.getData().getDestinationTrainStop() == this.nextStop) {
                disembarking.enqueue(passenger.getData());
            } else {
                remaining.enqueue(passenger.getData());
            }
            passenger = passenger.getNext();
        }

        this.passengers = remaining;
        return disembarking;
    }


    private void setNextStop() {
        TrainStop[] values = TrainStop.values();
        int index = nextStop.ordinal(); // this method is called while we are at a stop, so this.nextStop is the stop this is currently at
        Stop currentStop = stops.get(nextStop); // store current stop temporarily to calculate distance

        index += isSouthbound() ? 1 : -1;

        // if we went out of bounds, reverse it
        if (index == -1 || index == values.length) {
            reverseDirection();
            index += isSouthbound() ? 2 : -2;
        }
        nextStop = values[index];
        distanceToNextStop = currentStop.getDistance(stops.get(nextStop));
    }

    private void reverseDirection() {
        southbound = !southbound;
    }
}
