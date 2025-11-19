package com.tournamentapp.tournament_app.people.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

import java.util.UUID;

public class PersonNotFoundException extends DomainException {
    public PersonNotFoundException(UUID id) {
        super("Person not found with id: " + id);
    }

    public PersonNotFoundException(String dni) {
        super("Person not found with DNI: " + dni);
    }
}
