package psu.basepaths.model;

import java.util.Date;
import java.util.List;

public record TripDTO(
    long tripId,
    String name,
    Date startDate,
    Date endDate,
    double startLatitude,
    double startLongitude,
    double endLatitude,
    double endLongitude,
    boolean isGenerated,
    int maxHoursPerDay,
    long userId,
    List<TripStopDTO> tripStops
) {}
