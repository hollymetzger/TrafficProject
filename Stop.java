public class Stop extends Location {

    // Private Fields
    private Queue<Person> line;
    double longestCurrentlyWaiting;
    int lineLength;
    boolean train;

    // Constructors
    public Stop(double x, double y) {
        super(x, y);
        train = false;
    }
    public Stop(double x, double y, boolean met) {
        super(x, y);
        train = met;
    }
    // Accessors
    public Queue<Person> getLine() {
        return line;
    }
    public int getLineLength() {
        return line.getLength();
    }
    public boolean isTrain() {
        return train;
    }

    public double getLongestCurrentlyWaiting() {
        return longestCurrentlyWaiting;
    }

    // Public Methods
    public double getTotalWaitTime() {
        // todo: for each Person in queue, sum the time they have been
        //  waiting at this stop
        return 1.0;
    }

    public void add(Person person, boolean first) {
        line.enqueue(person);
        if (first) {
            double walkTime = getDistance(person.getHome()) / 2.5; // average person walks 2.5mph
            person.addTime(walkTime);
        }
    }


}
