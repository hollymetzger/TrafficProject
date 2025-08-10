public class Stop extends Location {

    // Private Fields
    private Queue<Person> line;
    int lineLength;
    boolean train;
    boolean busIncoming;
    String name;

    // Constructors
    public Stop(double x, double y) {
        super(x, y);
        train = false;
        name = "n/a";
        line = new Queue<Person>();
    }
    public Stop(double x, double y, boolean tr, String nm) {
        super(x, y);
        train = tr;
        name = nm;
        line = new Queue<Person>();
    }
    // Accessors
    public Queue<Person> getLine() {
        return line;
    }
    public int getLineLength() {
        return line.getLength();
    }
    public boolean getBusIncoming() {
        return busIncoming;
    }
    public String getName() { return name; }
    public boolean isTrain() {
        return train;
    }
    public String toString() { return "Location: " + super.toString() +
            "\nTrain: " + isTrain() +
            "\nName: " + getName();
    }

    // Mutators
    public void setBusIncoming(boolean bus) {
        busIncoming = bus;
    }

    // Public Methods
    public double getTotalWaitTime() {
        // todo: for each Person in queue, sum the time they have been
        //  waiting at this stop. need to add currentTime as a param bc person class only stores time that they
        //  arrived at the stop now how long they've been waiting
        return 1.0;
    }

    public void add(Person person, boolean first) {
        line.enqueue(person);
        if (first) {
            double walkTime = getDistance(person.getHome()) / 2.5; // average person walks 2.5mph
            person.setHomeWalkTime(walkTime);
        }
    }


}
