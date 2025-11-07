package psu.basepaths.utilities.graphObjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.jsonwebtoken.lang.Collections;
import psu.basepaths.model.Trip;

public class Graph {
    public static final String END_NODE_ID = "-2";

    public Map<Node,List<Edge>> graph = new HashMap<>();

    public Map<Node, Path> subPaths = new HashMap<>();

    public void addEdge(Node node, Edge edge){
        graph.computeIfAbsent(node, k -> new ArrayList<Edge>()).add(edge);
    }

    public Path dfs(Node node, Set<Integer> ballparks){
        if(node.gameId.equals(END_NODE_ID)){
            return new Path(List.of(node), 0, 0);
        }

        if(subPaths.containsKey(node)){
            return subPaths.get(node);
        }

        Path best = new Path(new ArrayList<Node>(), -1, -1);

        for(Edge edge : graph.getOrDefault(node, Collections.emptyList())){
            Node next = edge.destination;

            boolean newPark = !ballparks.contains(next.ballparkId);
            if(newPark){
                ballparks.add(next.ballparkId);
            }

            Set<Integer> nextBallparks = new HashSet<>(ballparks);
            Path candidatePath = dfs(next, nextBallparks);

            if(newPark){
                ballparks.remove(next.ballparkId);
            }

            int games = 1 + candidatePath.totalGames;
            int parks = (newPark ? 1 : 0) + candidatePath.uniqueBallparks;

            if(games > best.totalGames ||
                (games == best.totalGames && parks > best.uniqueBallparks)) {
                List<Node> newPath = new ArrayList<Node>();
                newPath.add(node);
                newPath.addAll(candidatePath.path);
                best = new Path(newPath, games, parks);
            }
        }

        subPaths.put(node, best);
        return best;
    }

    public Trip getBestTrip(){
        //not implemented
        return null;
    }
}
