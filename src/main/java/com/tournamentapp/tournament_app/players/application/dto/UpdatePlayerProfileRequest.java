package com.tournamentapp.tournament_app.players.application.dto;

import jakarta.validation.constraints.Size;

public record UpdatePlayerProfileRequest(
        @Size(max = 50, message = "Nickname cannot exceed 50 characters")
        String nickname,

        @Size(max = 500, message = "Photo URL cannot exceed 500 characters")
        String photoUrl
) {}
