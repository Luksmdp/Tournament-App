package com.tournamentapp.tournament_app.players.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

import java.util.UUID;

public class PlayerAlreadyExistsException extends DomainException {

    public PlayerAlreadyExistsException(UUID personId) {
        super("Player already exists for person with id: " + personId);
    }
}
