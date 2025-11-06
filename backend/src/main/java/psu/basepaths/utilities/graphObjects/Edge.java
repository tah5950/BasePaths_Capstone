package psu.basepaths.utilities.graphObjects;

import java.util.concurrent.TimeUnit;

public class Edge {
    public Node source;
    public Node destination;
    public double travelHours;

    public Edge(Node source, Node destination, double travelHours){
        this.source = source;
        this.destination = destination;
        this.travelHours = travelHours;
    }

    public int daysBetween(){
        long diffInMillis = destination.date.getTime() - source.date.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
