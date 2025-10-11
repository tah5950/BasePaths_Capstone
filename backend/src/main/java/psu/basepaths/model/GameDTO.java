package psu.basepaths.model;

import java.util.Date;

public record GameDTO(String gameId, Date date, String homeTeam, String awayTeam, int ballparkId) {}