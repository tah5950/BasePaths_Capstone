package psu.basepaths.utilities.graphObjects;

import java.util.List;
import java.util.Set;

public class Path {
    public List<Node> path;
    public int totalGames;
    public Set<Integer> ballparks;

    public Path(List<Node> path, int totalGames, Set<Integer> ballparks){
        this.path = path;
        this.totalGames = totalGames;
        this.ballparks = ballparks;
    }
}
