package com.tournamentapp.tournament_app.tournaments.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TournamentStarted(
        UUID tournamentId,
        LocalDate startDate,
        LocalDateTime occurredOn
) implements DomainEvent {

    public TournamentStarted(UUID tournamentId, LocalDate startDate) {
        this(tournamentId, startDate, LocalDateTime.now());
    }
}