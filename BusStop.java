public class BusStop extends Location {

    // Private Fields
    private Queue<Person> line;
    double longestCurrentlyWaiting;
    int lineLength;
    boolean metro;

    // Constructors
    public BusStop(double x, double y) {
        super(x, y);
        metro = false;
    }
    public BusStop(double x, double y, boolean met) {
        super(x, y);
        metro = met;
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

    public void add(Person person) {
        System.out.print("Added person to queue");
        // todo: handle adding a newly generated person to the busStop
        //  including adding walking time
    }


}
