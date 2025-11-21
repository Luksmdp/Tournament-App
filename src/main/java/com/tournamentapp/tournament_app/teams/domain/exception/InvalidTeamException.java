package com.tournamentapp.tournament_app.teams.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class InvalidTeamException extends DomainException {

    public InvalidTeamException(String message) {
        super(message);
    }
}