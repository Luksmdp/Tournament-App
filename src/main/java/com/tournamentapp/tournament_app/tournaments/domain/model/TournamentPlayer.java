package com.tournamentapp.tournament_app.tournaments.domain.model;

import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class TournamentPlayer {

    private final UUID id;
    private final TournamentId tournamentId;
    private TeamId teamId;
    private final PlayerId playerId;
    private Integer shirtNumber;
    private final LocalDateTime addedAt;

    // Constructor privado
    private TournamentPlayer(UUID id, TournamentId tournamentId, TeamId teamId,
                             PlayerId playerId, Integer shirtNumber) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.tournamentId = Objects.requireNonNull(tournamentId, "TournamentId cannot be null");
        this.teamId = Objects.requireNonNull(teamId, "TeamId cannot be null");
        this.playerId = Objects.requireNonNull(playerId, "PlayerId cannot be null");
        this.shirtNumber = shirtNumber;
        this.addedAt = LocalDateTime.now();
    }

    // Factory method para crear nuevo
    public static TournamentPlayer create(TournamentId tournamentId, TeamId teamId,
                                          PlayerId playerId, Integer shirtNumber) {
        return new TournamentPlayer(UUID.randomUUID(), tournamentId, teamId, playerId, shirtNumber);
    }

    // Factory method para reconstruir
    public static TournamentPlayer reconstruct(UUID id, TournamentId tournamentId, TeamId teamId,
                                               PlayerId playerId, Integer shirtNumber, LocalDateTime addedAt) {
        TournamentPlayer player = new TournamentPlayer(id, tournamentId, teamId, playerId, shirtNumber);
        return player;
    }

    // Cambiar de equipo dentro del torneo
    public void changeTeam(TeamId newTeamId) {
        this.teamId = Objects.requireNonNull(newTeamId, "TeamId cannot be null");
    }

    public void changeShirtNumber(Integer newNumber) {
        this.shirtNumber = newNumber;
    }

    // Getters
    public UUID getId() { return id; }
    public TournamentId getTournamentId() { return tournamentId; }
    public TeamId getTeamId() { return teamId; }
    public PlayerId getPlayerId() { return playerId; }
    public Integer getShirtNumber() { return shirtNumber; }
    public LocalDateTime getAddedAt() { return addedAt; }
}
