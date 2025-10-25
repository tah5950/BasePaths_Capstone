package psu.basepaths.model;

import java.util.Date;

public record TripStopDTO(
    long tripStopId,
    Date date,
    String location,
    int ballparkId,
    String gameId
) {}
