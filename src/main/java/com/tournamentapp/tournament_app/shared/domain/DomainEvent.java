package com.tournamentapp.tournament_app.shared.domain;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredOn();
}
