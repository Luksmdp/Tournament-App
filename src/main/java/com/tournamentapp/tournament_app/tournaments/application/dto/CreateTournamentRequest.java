package com.tournamentapp.tournament_app.tournaments.application.dto;

import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentType;
import jakarta.validation.constraints.*;

public record CreateTournamentRequest(
        @NotBlank(message = "Tournament name is required")
        @Size(min = 2, max = 200, message = "Tournament name must be between 2 and 200 characters")
        String name,

        @NotNull(message = "Tournament type is required")
        TournamentType type,

        @NotNull(message = "Year is required")
        @Min(value = 2020, message = "Year must be 2020 or later")
        @Max(value = 2100, message = "Year must be before 2100")
        Integer year,

        Integer winPoints,
        Integer drawPoints,
        Integer lossPoints
) {}
