package com.tournamentapp.tournament_app.tournaments.domain.exception;


import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class InsufficientTeamsException extends DomainException {
    public InsufficientTeamsException(String message) {
        super(message);
    }
}
