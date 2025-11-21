package com.tournamentapp.tournament_app.tournaments.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddTeamToTournamentRequest(
        @NotNull(message = "Team ID is required")
        UUID teamId
) {}
