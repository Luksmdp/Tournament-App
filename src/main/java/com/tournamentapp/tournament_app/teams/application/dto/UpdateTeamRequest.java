package com.tournamentapp.tournament_app.teams.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTeamRequest(
        @NotBlank(message = "Team name is required")
        @Size(min = 2, max = 200, message = "Team name must be between 2 and 200 characters")
        String name,

        @Size(max = 500, message = "Logo URL cannot exceed 500 characters")
        String logoUrl
) {}
