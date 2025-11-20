package com.tournamentapp.tournament_app.players.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

import java.util.UUID;

public class PlayerNotFoundException extends DomainException {

    public PlayerNotFoundException(UUID id) {
        super("Player not found with id: " + id);
    }

    public PlayerNotFoundException(String message) {
        super(message);
    }
}
