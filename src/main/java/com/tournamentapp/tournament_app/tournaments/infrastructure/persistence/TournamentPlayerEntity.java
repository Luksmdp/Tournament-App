package com.tournamentapp.tournament_app.tournaments.infrastructure.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tournament_players")
public class TournamentPlayerEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_team_id", nullable = false)
    private TournamentTeamEntity tournamentTeam;

    @Column(name = "tournament_id", nullable = false)
    private UUID tournamentId;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @Column(name = "shirt_number")
    private Integer shirtNumber;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    // Constructors
    public TournamentPlayerEntity() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public TournamentTeamEntity getTournamentTeam() { return tournamentTeam; }
    public void setTournamentTeam(TournamentTeamEntity tournamentTeam) { this.tournamentTeam = tournamentTeam; }

    public UUID getTournamentId() { return tournamentId; }
    public void setTournamentId(UUID tournamentId) { this.tournamentId = tournamentId; }

    public UUID getTeamId() { return teamId; }
    public void setTeamId(UUID teamId) { this.teamId = teamId; }

    public UUID getPlayerId() { return playerId; }
    public void setPlayerId(UUID playerId) { this.playerId = playerId; }

    public Integer getShirtNumber() { return shirtNumber; }
    public void setShirtNumber(Integer shirtNumber) { this.shirtNumber = shirtNumber; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
