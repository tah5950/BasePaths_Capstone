package psu.basepaths.model;

import jakarta.validation.constraints.NotNull;

public record BallparkDTO(
    @NotNull int ballparkId,
    @NotNull String name,
    @NotNull String teamName,
    @NotNull String city,
    @NotNull String state,
    @NotNull String country,
    @NotNull double lat,
    @NotNull double lon
) {}