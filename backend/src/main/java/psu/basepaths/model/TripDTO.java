package psu.basepaths.model;

import java.util.Date;
import java.util.List;

public record TripDTO(
    Long tripId,
    String name,
    Date startDate,
    Date endDate,
    Double startLatitude,
    Double startLongitude,
    Double endLatitude,
    Double endLongitude,
    Boolean isGenerated,
    Integer maxHoursPerDay,
    Long userId,
    List<TripStopDTO> tripStops
) {}
