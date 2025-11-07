package psu.basepaths.utilities.graphObjects;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;

import io.jsonwebtoken.lang.Collections;
import psu.basepaths.model.dto.TripDTO;
import psu.basepaths.model.dto.TripStopDTO;
import psu.basepaths.utilities.TripUtilities;

public class Graph {
    public static final String END_NODE_ID = "-2";
    public static final String TRAVEL_DAY_LOC = "Travel Day";

    public Map<Node,List<Edge>> graph = new HashMap<>();

    public Map<Node, Path> subPaths = new HashMap<>();

    public Node startNode;

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

    public TripDTO getBestTrip(TripDTO trip){
        Path bestPath = dfs(startNode, new HashSet<>());

        List<TripStopDTO> tripStops = new ArrayList<>();
        for(int i = 0; i < bestPath.path.size(); i++){
            Node current = bestPath.path.get(i);

            if(i != 0){
                Node prev = bestPath.path.get(i-1);

                long diffInMillis = prev.date.getTime() - current.date.getTime();
                int daysBetween = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

                if(daysBetween > 1){
                    for(int j = 1; j < daysBetween; j++){
                        Date travelDay = DateUtils.addDays(prev.date, j);
                        TripStopDTO travelStop = new TripStopDTO(
                            null,
                            travelDay,
                            TRAVEL_DAY_LOC,
                            null,
                            null
                        );
                        tripStops.add(travelStop);
                    }
                }
            }

            tripStops.add(TripUtilities.nodeToTripStop(current));
        }

        TripDTO tripDTO = new TripDTO(
            trip.tripId(),
            trip.name(),
            trip.startDate(),
            trip.endDate(),
            trip.startLatitude(),
            trip.startLongitude(),
            trip.endLatitude(),
            trip.endLongitude(),
            true,
            trip.maxHoursPerDay(),
            trip.userId(),
            tripStops
        );

        return tripDTO;
    }

    public void setStartNode(Node startNode){
        this.startNode = startNode;
    }
}
