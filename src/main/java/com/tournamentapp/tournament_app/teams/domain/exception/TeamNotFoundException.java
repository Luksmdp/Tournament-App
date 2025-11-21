package com.tournamentapp.tournament_app.teams.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

import java.util.UUID;

public class TeamNotFoundException extends DomainException {

    public TeamNotFoundException(UUID id) {
        super("Team not found with id: " + id);
    }

    public TeamNotFoundException(String name) {
        super("Team not found with name: " + name);
    }
}
