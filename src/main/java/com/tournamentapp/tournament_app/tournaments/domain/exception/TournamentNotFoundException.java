package com.tournamentapp.tournament_app.tournaments.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

import java.util.UUID;

public class TournamentNotFoundException extends DomainException {
    public TournamentNotFoundException(UUID id) {
        super("Tournament not found with id: " + id);
    }
}
