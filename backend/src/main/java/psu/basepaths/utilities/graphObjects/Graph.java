package psu.basepaths.utilities.graphObjects;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

import org.apache.commons.lang3.time.DateUtils;

import psu.basepaths.model.dto.TripDTO;
import psu.basepaths.model.dto.TripStopDTO;
import psu.basepaths.utilities.TripUtilities;

public class Graph {
    public static final String START_NODE_ID = "-1";
    public static final String END_NODE_ID = "-2";
    public static final String START_LOC = "Start";
    public static final String END_LOC = "End";
    public static final String TRAVEL_DAY_LOC = "Travel Day";

    public Map<Node,List<Edge>> graph = new HashMap<>();

    private Map<Node, Map<Set<Integer>, Path>> subPaths = new HashMap<>();

    public Node startNode;

    public void addEdge(Node node, Edge edge){
        graph.computeIfAbsent(node, k -> new ArrayList<Edge>()).add(edge);
    }

    public void sortEdgesByEndDate() {
        for (List<Edge> edgeList : graph.values()) {
            Collections.sort(edgeList);
        }
    }

    public Path dfs(Node node, Set<Integer> ballparks){
        if(node.gameId.equals(END_NODE_ID)){
            Set<Integer> ballparksCopy = new HashSet<>(ballparks);
            return new Path(List.of(node), 0, ballparksCopy);
        }

        Set<Integer> keySet = Collections.unmodifiableSet(new HashSet<>(ballparks));

        if (subPaths.containsKey(node) && subPaths.get(node).containsKey(keySet)) {
            return subPaths.get(node).get(keySet);
        }

        Path best = new Path(new ArrayList<Node>(), -1, new HashSet<>());

        boolean isRealGame = !node.gameId.equals(START_NODE_ID);

        for(Edge edge : graph.getOrDefault(node, Collections.emptyList())){
            Node next = edge.destination;

            Set<Integer> nextBallparks = new HashSet<>(ballparks);
            if(node.ballparkId != null){
                nextBallparks.add(node.ballparkId);
            }

            Path candidatePath = dfs(next, nextBallparks);

            int games = (isRealGame ? 1 : 0) + candidatePath.totalGames;
            int parks = candidatePath.ballparks.size();

            if(games > best.totalGames ||
                (games == best.totalGames && parks > best.ballparks.size())) {
                List<Node> newPath = new ArrayList<Node>();
                newPath.add(node);
                newPath.addAll(candidatePath.path);
                best = new Path(newPath, games, candidatePath.ballparks);
            }
        }

        subPaths.computeIfAbsent(node, k -> new HashMap<>()).put(keySet, best);

        return best;
    }

    public TripDTO getBestTrip(TripDTO trip){
        Path bestPath = dfs(startNode, new HashSet<>());

        List<TripStopDTO> tripStops = new ArrayList<>();
        for(int i = 0; i < bestPath.path.size(); i++){
            Node current = bestPath.path.get(i);

            if(i != 0){
                Node prev = bestPath.path.get(i-1);

                int daysBetween = TripUtilities.getDaysBetween(prev.date, current.date);

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

            if(current.gameId.equals(START_NODE_ID)){
                tripStops.add(new TripStopDTO(
                    null,
                    current.date,
                    START_LOC,
                    null,
                    null
                ));
            }
            else if(current.gameId.equals(END_NODE_ID)){
                tripStops.add(new TripStopDTO(
                    null,
                    current.date,
                    END_LOC,
                    null,
                    null
                ));
            }
            else{
                tripStops.add(TripUtilities.nodeToTripStop(current));
            }
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
