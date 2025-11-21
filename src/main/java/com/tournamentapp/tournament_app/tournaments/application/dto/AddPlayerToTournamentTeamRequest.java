package com.tournamentapp.tournament_app.tournaments.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.UUID;

public record AddPlayerToTournamentTeamRequest(
        @NotNull(message = "Player ID is required")
        UUID playerId,

        @Min(value = 1, message = "Shirt number must be at least 1")
        @Max(value = 99, message = "Shirt number cannot exceed 99")
        Integer shirtNumber
) {}
