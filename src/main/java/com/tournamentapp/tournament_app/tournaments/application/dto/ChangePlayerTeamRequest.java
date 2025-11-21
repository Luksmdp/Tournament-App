package com.tournamentapp.tournament_app.tournaments.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ChangePlayerTeamRequest(
        @NotNull(message = "New team ID is required")
        UUID newTeamId
) {}
