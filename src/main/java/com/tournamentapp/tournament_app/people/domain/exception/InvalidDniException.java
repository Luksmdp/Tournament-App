package com.tournamentapp.tournament_app.people.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class InvalidDniException extends DomainException {
    public InvalidDniException(String message) {
        super(message);
    }
}
