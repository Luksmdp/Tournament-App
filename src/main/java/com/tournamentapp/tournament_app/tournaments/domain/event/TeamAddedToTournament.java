package com.tournamentapp.tournament_app.tournaments.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamAddedToTournament(
        UUID tournamentId,
        UUID teamId,
        LocalDateTime occurredOn
) implements DomainEvent {

    public TeamAddedToTournament(UUID tournamentId, UUID teamId) {
        this(tournamentId, teamId, LocalDateTime.now());
    }
}
