package psu.basepaths.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tripid")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "start_latitude")
    private double startLatitude;

    @Column(name = "start_Longitude")
    private double startLongitude;

    @Column(name = "end_latitude")
    private double endLatitude;

    @Column(name = "end_Longitude")
    private double endLongitude;

    @Column(name = "is_generated")
    private boolean isGenerated;

    @Column(name = "max_hours_per_day")
    private int maxHoursPerDay;

    @Column(name = "userid")
    private Long userId;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TripStop> tripStops = new ArrayList<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public double getStartLatitude() { return startLatitude; }
    public void setStartLatitude(double startLatitude) { this.startLatitude = startLatitude; }

    public double getStartLongitude() { return startLongitude; }
    public void setStartLongitude(double startLongitude) { this.startLongitude = startLongitude; }

    public double getEndLatitude() { return endLatitude; }
    public void setEndLatitude(double endLatitude) { this.endLatitude = endLatitude; }

    public double getEndLongitude() { return endLongitude; }
    public void setEndLongitude(double endLongitude) { this.endLongitude = endLongitude; }

    public boolean getIsGenerated() { return isGenerated; }
    public void setIsGenerated(boolean isGenerated) { this.isGenerated = isGenerated; }

    public int getMaxHoursPerDay() { return maxHoursPerDay; }
    public void setMaxHoursPerDay(int maxHoursPerDay) { this.maxHoursPerDay = maxHoursPerDay; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<TripStop> getTripStops() { return tripStops; }
    public void setTripStops(List<TripStop> tripStops) { this.tripStops = tripStops; }
    public void addTripStop(TripStop stop) {
        if(tripStops == null) {
            tripStops = new ArrayList<>();
        }
        tripStops.add(stop);
    }
}
