package com.tournamentapp.tournament_app.teams.domain.model;

import com.tournamentapp.tournament_app.shared.domain.AggregateRoot;
import com.tournamentapp.tournament_app.teams.domain.event.TeamCreated;
import com.tournamentapp.tournament_app.teams.domain.event.TeamDissolved;

import java.time.LocalDateTime;
import java.util.Objects;

public class Team extends AggregateRoot {

    private final TeamId id;
    private String name;
    private String logoUrl;
    private TeamStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor privado
    private Team(TeamId id, String name, String logoUrl, TeamStatus status) {
        this.id = Objects.requireNonNull(id, "TeamId cannot be null");
        this.name = validateName(name);
        this.logoUrl = logoUrl;
        this.status = status != null ? status : TeamStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Registrar evento de dominio
        registerEvent(new TeamCreated(id.value(), name));
    }

    // Factory method para crear nuevo equipo
    public static Team create(String name, String logoUrl) {
        return new Team(TeamId.generate(), name, logoUrl, TeamStatus.ACTIVE);
    }

    // Factory method para reconstruir desde BD
    public static Team reconstruct(TeamId id, String name, String logoUrl,
                                   TeamStatus status, LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {
        Team team = new Team(id, name, logoUrl, status);
        // No registrar evento al reconstruir
        team.clearDomainEvents();
        return team;
    }

    // Métodos de negocio
    public void updateName(String newName) {
        this.name = validateName(newName);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLogoUrl(String newLogoUrl) {
        this.logoUrl = newLogoUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == TeamStatus.DISSOLVED) {
            throw new IllegalStateException("Cannot activate a dissolved team");
        }
        this.status = TeamStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == TeamStatus.DISSOLVED) {
            throw new IllegalStateException("Cannot deactivate a dissolved team");
        }
        this.status = TeamStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void dissolve() {
        this.status = TeamStatus.DISSOLVED;
        this.updatedAt = LocalDateTime.now();

        registerEvent(new TeamDissolved(id.value(), name));
    }

    public boolean isActive() {
        return this.status == TeamStatus.ACTIVE;
    }

    public boolean canParticipateInTournaments() {
        return this.status == TeamStatus.ACTIVE;
    }

    private String validateName(String name) {
        Objects.requireNonNull(name, "Team name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be blank");
        }
        if (name.length() > 200) {
            throw new IllegalArgumentException("Team name cannot exceed 200 characters");
        }
        return name.trim();
    }

    // Getters
    public TeamId getId() { return id; }
    public String getName() { return name; }
    public String getLogoUrl() { return logoUrl; }
    public TeamStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
