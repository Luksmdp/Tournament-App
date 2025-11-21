package com.tournamentapp.tournament_app.teams.application.dto;

import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamResponse(
        UUID id,
        String name,
        String logoUrl,
        TeamStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId().value(),
                team.getName(),
                team.getLogoUrl(),
                team.getStatus(),
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }
}
