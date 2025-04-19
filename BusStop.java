public class BusStop extends Location {

    // Private Fields
    private Queue<Person> line;
    double longestCurrentlyWaiting;
    int lineLength;

    // Constructor
    public BusStop(double x, double y) {
        super(x, y);
    }
    // Accessors
    public Queue<Person> getLine() {
        return line;
    }

    public double getLongestCurrentlyWaiting() {
        return longestCurrentlyWaiting;
    }

    // Public Methods
    public double getTotalWaitTime() {
        // todo: for each Person in queue, sum the time they have been
        //  waiting at this stop
    }


}
