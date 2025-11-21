package com.tournamentapp.tournament_app.tournaments.application.dto;

import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TournamentTeamResponse(
        UUID id,
        UUID tournamentId,
        UUID teamId,
        TeamResponse team,
        boolean isActive,
        int playerCount,
        List<TournamentPlayerResponse> players,
        LocalDateTime addedAt
) {}
