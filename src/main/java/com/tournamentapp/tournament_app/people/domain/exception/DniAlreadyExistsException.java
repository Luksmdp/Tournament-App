package com.tournamentapp.tournament_app.people.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class DniAlreadyExistsException extends DomainException {
    public DniAlreadyExistsException(String dni) {
        super("Person with DNI " + dni + " already exists");
    }
}
