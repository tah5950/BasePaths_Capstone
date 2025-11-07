package psu.basepaths.utilities.graphObjects;

import java.util.concurrent.TimeUnit;

import psu.basepaths.utilities.TripUtilities;

public class Edge implements Comparable<Edge>{
    public Node source;
    public Node destination;
    public double travelHours;

    public Edge(Node source, Node destination, double travelHours){
        this.source = source;
        this.destination = destination;
        this.travelHours = travelHours;
    }

    public int daysBetween(){
        return TripUtilities.getDaysBetween(source.date, destination.date);
    }

    @Override
    public int compareTo(Edge edge) {
        return this.destination.date.compareTo(edge.destination.date);
    }
}
