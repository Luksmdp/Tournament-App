package com.tournamentapp.tournament_app.tournaments.application.dto;

import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record TournamentPlayerResponse(
        UUID id,
        UUID tournamentId,
        UUID teamId,
        UUID playerId,
        PlayerResponse player,
        Integer shirtNumber,
        LocalDateTime addedAt
) {}
