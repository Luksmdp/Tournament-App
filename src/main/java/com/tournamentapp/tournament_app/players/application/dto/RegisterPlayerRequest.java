package com.tournamentapp.tournament_app.players.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RegisterPlayerRequest(
        @NotNull(message = "Person ID is required")
        UUID personId,

        @Size(max = 50, message = "Nickname cannot exceed 50 characters")
        String nickname,

        @Size(max = 500, message = "Photo URL cannot exceed 500 characters")
        String photoUrl
) {}
