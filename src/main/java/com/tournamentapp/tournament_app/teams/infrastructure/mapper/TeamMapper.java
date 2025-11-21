package com.tournamentapp.tournament_app.teams.infrastructure.mapper;

import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.model.TeamStatus;
import com.tournamentapp.tournament_app.teams.infrastructure.persistence.TeamEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public Team toDomain(TeamEntity entity) {
        TeamId id = new TeamId(entity.getId());
        TeamStatus status = entity.getStatus();

        return Team.reconstruct(
                id,
                entity.getName(),
                entity.getLogoUrl(),
                status,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public TeamEntity toEntity(Team team) {
        TeamEntity entity = new TeamEntity();
        entity.setId(team.getId().value());
        entity.setName(team.getName());
        entity.setLogoUrl(team.getLogoUrl());
        entity.setStatus(team.getStatus());
        entity.setCreatedAt(team.getCreatedAt());
        entity.setUpdatedAt(team.getUpdatedAt());

        return entity;
    }
}
