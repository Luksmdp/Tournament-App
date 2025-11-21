package com.tournamentapp.tournament_app.tournaments.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentType;

import java.time.LocalDateTime;
import java.util.UUID;

public record TournamentCreated(
        UUID tournamentId,
        String name,
        TournamentType type,
        Integer year,
        LocalDateTime occurredOn
) implements DomainEvent {

    public TournamentCreated(UUID tournamentId, String name, TournamentType type, Integer year) {
        this(tournamentId, name, type, year, LocalDateTime.now());
    }
}
