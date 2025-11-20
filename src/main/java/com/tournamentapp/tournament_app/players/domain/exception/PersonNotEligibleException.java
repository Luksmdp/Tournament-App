package com.tournamentapp.tournament_app.players.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class PersonNotEligibleException extends DomainException {

    public PersonNotEligibleException(String message) {
        super(message);
    }
}
