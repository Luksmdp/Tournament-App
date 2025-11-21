package com.tournamentapp.tournament_app.teams.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class TeamNameAlreadyExistsException extends DomainException {

    public TeamNameAlreadyExistsException(String name) {
        super("Team with name '" + name + "' already exists");
    }
}
