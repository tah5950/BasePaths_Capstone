package psu.basepaths.model.dto;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

public record GameDTO(
    @NotNull String gameId,
    @NotNull Date date,
    @NotNull String homeTeam,
    @NotNull String awayTeam,
    @NotNull int ballparkId
) {}