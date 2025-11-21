package com.tournamentapp.tournament_app.tournaments.infrastructure.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tournament_teams")
public class TournamentTeamEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private TournamentEntity tournament;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @OneToMany(mappedBy = "tournamentTeam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TournamentPlayerEntity> players = new HashSet<>();

    // Constructors
    public TournamentTeamEntity() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public TournamentEntity getTournament() { return tournament; }
    public void setTournament(TournamentEntity tournament) { this.tournament = tournament; }

    public UUID getTeamId() { return teamId; }
    public void setTeamId(UUID teamId) { this.teamId = teamId; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    public Set<TournamentPlayerEntity> getPlayers() { return players; }
    public void setPlayers(Set<TournamentPlayerEntity> players) { this.players = players; }
}
