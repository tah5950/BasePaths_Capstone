package psu.basepaths.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "game_id")
    private String id;

    @Column(name = "date")
    private Date date;

    @Column(name = "home_team")
    private String homeTeam;

    @Column(name = "away_team")
    private String awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballpark_id", nullable = false)
    private Ballpark ballpark;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public Ballpark getBallpark() { return ballpark; }
    public void setBallpark(Ballpark ballpark) { this.ballpark = ballpark; }
}
