package com.tournamentapp.tournament_app.players.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerProfileUpdated(
        UUID playerId,
        UUID personId,
        LocalDateTime occurredOn
) implements DomainEvent {

    public PlayerProfileUpdated(UUID playerId, UUID personId) {
        this(playerId, personId, LocalDateTime.now());
    }
}
