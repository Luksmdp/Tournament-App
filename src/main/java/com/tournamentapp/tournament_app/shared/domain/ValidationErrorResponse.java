package com.tournamentapp.tournament_app.shared.domain;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        int status,
        String error,
        Map<String,String> validationErrors,
        LocalDateTime timestamp
) {}