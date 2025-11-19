package com.tournamentapp.tournament_app.shared.domain;

import java.time.LocalDateTime;

// DTOs para respuestas de error
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}
