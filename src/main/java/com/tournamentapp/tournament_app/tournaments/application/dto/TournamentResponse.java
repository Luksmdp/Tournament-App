package com.tournamentapp.tournament_app.tournaments.application.dto;

import com.tournamentapp.tournament_app.tournaments.domain.model.PointsRuleSet;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentStatus;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TournamentResponse(
        UUID id,
        String name,
        TournamentType type,
        Integer year,
        TournamentStatus status,
        LocalDate startDate,
        LocalDate endDate,
        PointsRuleSet pointsRules,
        Integer teamsCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TournamentResponse from(Tournament tournament) {
        return new TournamentResponse(
                tournament.getId().value(),
                tournament.getName(),
                tournament.getType(),
                tournament.getYear(),
                tournament.getStatus(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getPointsRules(),
                tournament.getTeamsCount(),
                tournament.getCreatedAt(),
                tournament.getUpdatedAt()
        );
    }
}
