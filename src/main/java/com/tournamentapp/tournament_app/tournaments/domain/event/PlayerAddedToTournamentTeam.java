package com.tournamentapp.tournament_app.tournaments.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerAddedToTournamentTeam(
        UUID tournamentId,
        UUID teamId,
        UUID playerId,
        LocalDateTime occurredOn
) implements DomainEvent {

    public PlayerAddedToTournamentTeam(UUID tournamentId, UUID teamId, UUID playerId) {
        this(tournamentId, teamId, playerId, LocalDateTime.now());
    }
}
