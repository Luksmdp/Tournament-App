package com.tournamentapp.tournament_app.tournaments.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTournamentRequest(
        @NotBlank(message = "Tournament name is required")
        @Size(min = 2, max = 200, message = "Tournament name must be between 2 and 200 characters")
        String name
) {}
