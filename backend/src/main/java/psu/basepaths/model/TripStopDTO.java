package psu.basepaths.model;

import java.util.Date;

public record TripStopDTO(
    Long tripStopId,
    Date date,
    String location,
    Integer ballparkId,
    String gameId
) {}
