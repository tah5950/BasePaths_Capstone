package psu.basepaths.utilities.graphObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import psu.basepaths.model.BallparkDTO;
import psu.basepaths.model.Trip;

public class Graph {
    public Map<Node,List<Edge>> graph = new HashMap<>();

    public void addEdge(Node node, Edge edge){
        graph.computeIfAbsent(node, k -> new ArrayList<Edge>()).add(edge);
    }

    public Path dfs(Node node, Set<BallparkDTO> ballparks){
        //not implemented
        return null;
    }

    public Trip getBestTrip(){
        //not implemented
        return null;
    }
}
