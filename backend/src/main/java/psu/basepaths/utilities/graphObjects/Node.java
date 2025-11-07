package psu.basepaths.utilities.graphObjects;

import java.util.Date;
import java.util.Objects;

public class Node{
    public String gameId;
    public Date date;
    public double latitude;
    public double longitude;
    public Integer ballparkId;

    public Node(String gameId, Date date, double latitude, double longitude, Integer ballparkId) {
        this.gameId = gameId;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ballparkId = ballparkId;
    }

    public boolean isGameNode(){
        return gameId != null;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Node)){
            return false;
        }
        Node n = (Node) o;
        return Objects.equals(gameId, n.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }

    @Override
    public String toString() {
        return gameId;
    }
}
