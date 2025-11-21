package com.tournamentapp.tournament_app.tournaments.domain.exception;

import com.tournamentapp.tournament_app.shared.domain.DomainException;

import java.util.UUID;

public class PlayerNotInTournamentException extends DomainException {
    public PlayerNotInTournamentException(UUID playerId) {
        super("Player not found in tournament: " + playerId);
    }
}
