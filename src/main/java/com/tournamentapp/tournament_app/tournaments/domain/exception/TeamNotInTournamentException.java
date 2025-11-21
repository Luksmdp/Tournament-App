package com.tournamentapp.tournament_app.tournaments.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

import java.util.UUID;

public class TeamNotInTournamentException extends DomainException {
    public TeamNotInTournamentException(UUID teamId) {
        super("Team not found in tournament: " + teamId);
    }
}
