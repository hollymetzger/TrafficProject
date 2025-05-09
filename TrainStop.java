public enum TrainStop {
    FREDERICK(0),
    GAITHERSBURG(5),
    ROCKVILLE(6),
    BETHESDA(3),
    WASHINGTON_DC(4);

    private final int weight;

    TrainStop(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public static TrainStop getWeightedStop() {
        TrainStop[] stops = TrainStop.values();

        int totalWeight = 0;
        for (TrainStop stop : stops) {
            totalWeight += stop.getWeight();
        }

        int randomInt = (int) (Math.random() * totalWeight);
        int sum = 0;
        for (TrainStop stop : stops) {
            sum += stop.getWeight();
            if (randomInt < sum) {
                return stop;
            }
        }

        return stops[stops.length - 1];
    }
}



//todo: would love to make this more consistent and import destination cities from csv like we do
// for frederick cities. that or make an enum for the frederick cities just to make it consistent