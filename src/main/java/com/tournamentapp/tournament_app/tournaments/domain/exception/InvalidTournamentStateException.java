package com.tournamentapp.tournament_app.tournaments.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class InvalidTournamentStateException extends DomainException {
    public InvalidTournamentStateException(String message) {
        super(message);
    }
}
