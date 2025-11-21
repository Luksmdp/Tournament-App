package com.tournamentapp.tournament_app.tournaments.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TournamentFinished(
        UUID tournamentId,
        LocalDate endDate,
        LocalDateTime occurredOn
) implements DomainEvent {

    public TournamentFinished(UUID tournamentId, LocalDate endDate) {
        this(tournamentId, endDate, LocalDateTime.now());
    }
}
