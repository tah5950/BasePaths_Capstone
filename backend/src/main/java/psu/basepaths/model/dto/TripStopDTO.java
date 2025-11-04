package psu.basepaths.model.dto;

import java.util.Date;

public record TripStopDTO(
    Long tripStopId,
    Date date,
    String location,
    Integer ballparkId,
    String gameId
) {}
