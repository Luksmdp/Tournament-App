package com.tournamentapp.tournament_app.teams.domain.event;

import com.tournamentapp.tournament_app.shared.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamCreated(
        UUID teamId,
        String teamName,
        LocalDateTime occurredOn
) implements DomainEvent {

    public TeamCreated(UUID teamId, String teamName) {
        this(teamId, teamName, LocalDateTime.now());
    }
}
