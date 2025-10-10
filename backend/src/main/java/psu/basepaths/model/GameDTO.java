package psu.basepaths.model;

import java.util.Date;

public record GameDTO(Long id, Date date, String homeTeam, String awayTeam, Ballpark ballpark) {}