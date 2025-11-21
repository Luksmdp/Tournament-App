package com.tournamentapp.tournament_app.tournaments.infrastructure.mapper;

import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.tournaments.domain.model.*;
import com.tournamentapp.tournament_app.tournaments.infrastructure.persistence.TournamentEntity;
import com.tournamentapp.tournament_app.tournaments.infrastructure.persistence.TournamentPlayerEntity;
import com.tournamentapp.tournament_app.tournaments.infrastructure.persistence.TournamentTeamEntity;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TournamentMapper {

    public Tournament toDomain(TournamentEntity entity) {
        TournamentId id = new TournamentId(entity.getId());
        TournamentType type = entity.getTournamentType();
        TournamentStatus status = entity.getStatus();

        PointsRuleSet pointsRules = new PointsRuleSet(
                entity.getWinPoints(),
                entity.getDrawPoints(),
                entity.getLossPoints(),
                entity.getUsesGoalDifference()
        );

        Set<TournamentTeam> teams = entity.getTeams().stream()
                .map(this::teamToDomain)
                .collect(Collectors.toSet());

        return Tournament.reconstruct(
                id,
                entity.getName(),
                type,
                entity.getYear(),
                status,
                entity.getStartDate(),
                entity.getEndDate(),
                pointsRules,
                teams,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public TournamentEntity toEntity(Tournament tournament) {
        TournamentEntity entity = new TournamentEntity();
        entity.setId(tournament.getId().value());
        entity.setName(tournament.getName());
        entity.setTournamentType(tournament.getType());
        entity.setYear(tournament.getYear());
        entity.setStatus(tournament.getStatus());
        entity.setStartDate(tournament.getStartDate());
        entity.setEndDate(tournament.getEndDate());
        entity.setWinPoints(tournament.getPointsRules().winPoints());
        entity.setDrawPoints(tournament.getPointsRules().drawPoints());
        entity.setLossPoints(tournament.getPointsRules().lossPoints());
        entity.setUsesGoalDifference(tournament.getPointsRules().usesGoalDifference());
        entity.setCreatedAt(tournament.getCreatedAt());
        entity.setUpdatedAt(tournament.getUpdatedAt());

        Set<TournamentTeamEntity> teamEntities = tournament.getTeams().stream()
                .map(team -> teamToEntity(team, entity))
                .collect(Collectors.toSet());
        entity.setTeams(teamEntities);

        return entity;
    }

    private TournamentTeam teamToDomain(TournamentTeamEntity entity) {
        TournamentId tournamentId = new TournamentId(entity.getTournament().getId());
        TeamId teamId = new TeamId(entity.getTeamId());

        Set<TournamentPlayer> players = entity.getPlayers().stream()
                .map(this::playerToDomain)
                .collect(Collectors.toSet());

        return TournamentTeam.reconstruct(
                entity.getId(),
                tournamentId,
                teamId,
                entity.getIsActive(),
                entity.getAddedAt(),
                players
        );
    }

    private TournamentTeamEntity teamToEntity(TournamentTeam team, TournamentEntity tournament) {
        TournamentTeamEntity entity = new TournamentTeamEntity();
        entity.setId(team.getId());
        entity.setTournament(tournament);
        entity.setTeamId(team.getTeamId().value());
        entity.setIsActive(team.isActive());
        entity.setAddedAt(team.getAddedAt());

        Set<TournamentPlayerEntity> playerEntities = team.getPlayers().stream()
                .map(player -> playerToEntity(player, entity))
                .collect(Collectors.toSet());
        entity.setPlayers(playerEntities);

        return entity;
    }

    private TournamentPlayer playerToDomain(TournamentPlayerEntity entity) {
        TournamentId tournamentId = new TournamentId(entity.getTournamentId());
        TeamId teamId = new TeamId(entity.getTeamId());
        PlayerId playerId = new PlayerId(entity.getPlayerId());

        return TournamentPlayer.reconstruct(
                entity.getId(),
                tournamentId,
                teamId,
                playerId,
                entity.getShirtNumber(),
                entity.getAddedAt()
        );
    }

    private TournamentPlayerEntity playerToEntity(TournamentPlayer player, TournamentTeamEntity tournamentTeam) {
        TournamentPlayerEntity entity = new TournamentPlayerEntity();
        entity.setId(player.getId());
        entity.setTournamentTeam(tournamentTeam);
        entity.setTournamentId(player.getTournamentId().value());
        entity.setTeamId(player.getTeamId().value());
        entity.setPlayerId(player.getPlayerId().value());
        entity.setShirtNumber(player.getShirtNumber());
        entity.setAddedAt(player.getAddedAt());

        return entity;
    }
}
