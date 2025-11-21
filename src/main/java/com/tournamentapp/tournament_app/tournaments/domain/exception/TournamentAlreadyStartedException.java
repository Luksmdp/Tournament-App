package com.tournamentapp.tournament_app.tournaments.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

public class TournamentAlreadyStartedException extends DomainException {
    public TournamentAlreadyStartedException(String message) {
        super(message);
    }
}
