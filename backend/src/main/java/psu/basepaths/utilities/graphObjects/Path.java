package psu.basepaths.utilities.graphObjects;

import java.util.List;

public class Path {
    public List<Node> path;
    public int totalGames;
    public int uniqueBallparks;

    public Path(List<Node> path, int totalGames, int uniqueBallparks){
        this.path = path;
        this.totalGames = totalGames;
        this.uniqueBallparks = uniqueBallparks;
    }
}
