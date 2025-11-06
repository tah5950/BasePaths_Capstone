package psu.basepaths.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import psu.basepaths.model.dto.BallparkDTO;
import psu.basepaths.model.dto.GameDTO;
import psu.basepaths.model.dto.TripDTO;
import psu.basepaths.model.dto.TripStopDTO;
import psu.basepaths.utilities.graphObjects.Edge;
import psu.basepaths.utilities.graphObjects.Graph;
import psu.basepaths.utilities.graphObjects.Node;

public class TripUtilities {
    private static final double EARTH_RADIUS_MILES = 3963.1;
    private static final int AVERAGE_SPEED_MPH = 65;
    private static Map<Integer, BallparkDTO> ballparkMap = new HashMap<Integer, BallparkDTO>();

    public TripDTO generateTrip(TripDTO trip, List<BallparkDTO> ballparks, List<GameDTO> games){
        populateBallparkMap(ballparks);
        Map<Integer, Map<Integer, Double>> ballparkDist = ballparkDistanceMap(ballparks);
        Graph graph = buildGraph(games, ballparkDist, trip.maxHoursPerDay());
        addStartandEndEdges(graph, trip, games, ballparkDist);
        return null;
    }
    
    private Graph buildGraph(List<GameDTO> games, Map<Integer, Map<Integer, Double>> ballparkDistances, int maxTravelHoursPerDay) {
        Graph graph = new Graph();

        for(GameDTO game1: games) {
            Node startNode = new Node(game1.gameId(), game1.date(), 
                                ballparkMap.get(game1.ballparkId()).lat(), 
                                ballparkMap.get(game1.ballparkId()).lon(), 
                                game1.ballparkId());

            for(GameDTO game2: games) {
                Node endNode = new Node(game2.gameId(), game2.date(), 
                                ballparkMap.get(game2.ballparkId()).lat(), 
                                ballparkMap.get(game2.ballparkId()).lon(), 
                                game2.ballparkId());
                
                if(game1.gameId() != game2.gameId()) {
                    if(game1.date().before(game2.date())) {
                        double travelTime = ballparkDistances.get(game1.ballparkId()).get(game2.ballparkId()) / AVERAGE_SPEED_MPH;
                        Edge edge = new Edge(startNode, endNode, travelTime);

                        if(edge.travelHours <= edge.daysBetween() * maxTravelHoursPerDay){
                            graph.addEdge(startNode, edge);
                        }
                    }
                }
            }
        }
        return graph;
    }

    private void addStartandEndEdges(Graph graph, TripDTO trip, List<GameDTO> games, Map<Integer, Map<Integer, Double>> ballparkDistances) {
        Node startNode = new Node("-1", trip.startDate(), trip.startLatitude(), trip.startLongitude(), null);
        Node endNode = new Node("-2", trip.endDate(), trip.endLatitude(), trip.endLongitude(), null);

        for (GameDTO game : games) {
            Node gameNode = new Node(game.gameId(), game.date(), 
                                ballparkMap.get(game.ballparkId()).lat(), 
                                ballparkMap.get(game.ballparkId()).lon(), 
                                game.ballparkId());

            double startDistance = calculateDistance(startNode.latitude, startNode.longitude, gameNode.latitude, gameNode.longitude);
            double startTravelHours = startDistance / AVERAGE_SPEED_MPH;
            Edge startEdge = new Edge(startNode, gameNode, startTravelHours);

            if (startEdge.travelHours <= startEdge.daysBetween() * trip.maxHoursPerDay()) {
                graph.addEdge(startNode, startEdge);
            }

            double endDistance = calculateDistance(gameNode.latitude, gameNode.longitude, endNode.latitude, endNode.longitude);
            double endTravelHours = endDistance / AVERAGE_SPEED_MPH;
            Edge endEdge = new Edge(gameNode, endNode, endTravelHours);

            if (endEdge.travelHours <= endEdge.daysBetween() * trip.maxHoursPerDay()) {
                graph.addEdge(gameNode, endEdge);
            }
        }
    }
    
    private Map<Integer, Map<Integer, Double>> ballparkDistanceMap(List<BallparkDTO> ballparks) {
        Map<Integer, Map<Integer, Double>> ballparkDistances = new HashMap<>();

        for(int i = 0; i < ballparks.size(); i++) {
            BallparkDTO park1 = ballparks.get(i);

            for(int j = i+1; j < ballparks.size(); j++){
                BallparkDTO park2 = ballparks.get(j);

                double distance = calculateDistance(park1.lat(), park1.lon(), park2.lat(), park2.lon());

                ballparkDistances.putIfAbsent(park1.ballparkId(), new HashMap<>()).put(park2.ballparkId(), distance);
                ballparkDistances.putIfAbsent(park2.ballparkId(), new HashMap<>()).put(park1.ballparkId(), distance);
            }
        }

        return ballparkDistances;
    }
    
    private double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude ){
        double dLat = Math.toRadians((endLatitude - startLatitude));
        double dLon = Math.toRadians((endLongitude - startLongitude));

        startLatitude = Math.toRadians(startLatitude);
        endLatitude = Math.toRadians(endLatitude);

        double a = haversine(dLat) + Math.cos(startLatitude) * Math.cos(endLatitude) * haversine(dLon);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_MILES * c;
    }
    
    private double haversine(double val) {
        return Math.pow(Math.sin(val/2), 2);
    }

    private void populateBallparkMap(List<BallparkDTO> ballparks){
        for(BallparkDTO ballpark : ballparks){
            ballparkMap.put(ballpark.ballparkId(), ballpark);
        }
    }

    private TripStopDTO nodeToTripStop(Node node) {

        return new TripStopDTO(
            null,
            node.date, 
            ballparkMap.get(node.ballparkId).city(),
            node.ballparkId, 
            node.gameId
        );
    }
}
