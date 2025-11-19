package com.tournamentapp.tournament_app.people.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonRegistered(
        UUID personId,
        String dni,
        String fullName,
        LocalDateTime occurredOn
) implements DomainEvent {

    public PersonRegistered(UUID personId, String dni, String fullName) {
        this(personId, dni, fullName, LocalDateTime.now());
    }
}
