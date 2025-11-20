package com.tournamentapp.tournament_app.players.domain.model;

import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.players.domain.event.PlayerProfileUpdated;
import com.tournamentapp.tournament_app.players.domain.event.PlayerRegistered;
import com.tournamentapp.tournament_app.shared.domain.AggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;

public class Player extends AggregateRoot {

    private final PlayerId id;
    private final PersonId personId;  // Referencia a Person (FK)
    private PlayerProfile profile;
    private PlayerStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor privado
    private Player(PlayerId id, PersonId personId, PlayerProfile profile, PlayerStatus status) {
        this.id = Objects.requireNonNull(id, "PlayerId cannot be null");
        this.personId = Objects.requireNonNull(personId, "PersonId cannot be null");
        this.profile = profile != null ? profile : PlayerProfile.empty();
        this.status = status != null ? status : PlayerStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Registrar evento de dominio
        registerEvent(new PlayerRegistered(id.value(), personId.value()));
    }

    // Factory method para crear nuevo jugador
    public static Player create(PersonId personId, PlayerProfile profile) {
        return new Player(PlayerId.generate(), personId, profile, PlayerStatus.ACTIVE);
    }

    // Factory method para reconstruir desde BD
    public static Player reconstruct(PlayerId id, PersonId personId, PlayerProfile profile,
                                     PlayerStatus status, LocalDateTime createdAt,
                                     LocalDateTime updatedAt) {
        Player player = new Player(id, personId, profile, status);
        // No registrar evento al reconstruir
        player.clearDomainEvents();
        return player;
    }

    // Métodos de negocio
    public void updateProfile(PlayerProfile newProfile) {
        this.profile = Objects.requireNonNull(newProfile, "Profile cannot be null");
        this.updatedAt = LocalDateTime.now();

        registerEvent(new PlayerProfileUpdated(id.value(), personId.value()));
    }

    public void activate() {
        if (this.status == PlayerStatus.RETIRED) {
            throw new IllegalStateException("Cannot activate a retired player");
        }
        this.status = PlayerStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = PlayerStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void suspend() {
        this.status = PlayerStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void retire() {
        this.status = PlayerStatus.RETIRED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == PlayerStatus.ACTIVE;
    }

    public boolean canPlay() {
        return this.status == PlayerStatus.ACTIVE;
    }

    // Getters
    public PlayerId getId() { return id; }
    public PersonId getPersonId() { return personId; }
    public PlayerProfile getProfile() { return profile; }
    public PlayerStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}