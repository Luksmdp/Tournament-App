package com.tournamentapp.tournament_app.tournaments.domain.model;

import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;

import java.time.LocalDateTime;
import java.util.*;

public class TournamentTeam {

    private final UUID id;
    private final TournamentId tournamentId;
    private final TeamId teamId;
    private boolean isActive;
    private final LocalDateTime addedAt;
    private final Set<TournamentPlayer> players;

    // Constructor privado
    private TournamentTeam(UUID id, TournamentId tournamentId, TeamId teamId) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.tournamentId = Objects.requireNonNull(tournamentId, "TournamentId cannot be null");
        this.teamId = Objects.requireNonNull(teamId, "TeamId cannot be null");
        this.isActive = true;
        this.addedAt = LocalDateTime.now();
        this.players = new HashSet<>();
    }

    // Factory method para crear nuevo
    public static TournamentTeam create(TournamentId tournamentId, TeamId teamId) {
        return new TournamentTeam(UUID.randomUUID(), tournamentId, teamId);
    }

    // Factory method para reconstruir
    public static TournamentTeam reconstruct(UUID id, TournamentId tournamentId, TeamId teamId,
                                             boolean isActive, LocalDateTime addedAt,
                                             Set<TournamentPlayer> players) {
        TournamentTeam team = new TournamentTeam(id, tournamentId, teamId);
        team.isActive = isActive;
        team.players.addAll(players);
        return team;
    }

    // Agregar jugador al equipo en el torneo
    public void addPlayer(TournamentPlayer player) {
        Objects.requireNonNull(player, "Player cannot be null");

        // Verificar que el jugador no esté ya en el equipo
        if (players.stream().anyMatch(p -> p.getPlayerId().equals(player.getPlayerId()))) {
            throw new IllegalArgumentException("Player already in team");
        }

        // Verificar que el número de camiseta no esté ocupado
        if (player.getShirtNumber() != null &&
                players.stream().anyMatch(p -> player.getShirtNumber().equals(p.getShirtNumber()))) {
            throw new IllegalArgumentException("Shirt number already taken");
        }

        players.add(player);
    }

    // Remover jugador del equipo
    public void removePlayer(PlayerId playerId) {
        players.removeIf(p -> p.getPlayerId().equals(playerId));
    }

    // Verificar si tiene el mínimo de jugadores
    public boolean hasMinimumPlayers(int minimumRequired) {
        return players.size() >= minimumRequired;
    }

    // Getters
    public UUID getId() { return id; }
    public TournamentId getTournamentId() { return tournamentId; }
    public TeamId getTeamId() { return teamId; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public Set<TournamentPlayer> getPlayers() { return Collections.unmodifiableSet(players); }
    public int getPlayerCount() { return players.size(); }
}
