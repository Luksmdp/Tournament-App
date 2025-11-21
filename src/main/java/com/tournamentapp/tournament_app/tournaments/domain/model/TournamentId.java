package com.tournamentapp.tournament_app.tournaments.domain.model;

import java.util.Objects;
import java.util.UUID;

public record TournamentId(UUID value) {

    public TournamentId {
        Objects.requireNonNull(value, "TournamentId cannot be null");
    }

    public static TournamentId generate() {
        return new TournamentId(UUID.randomUUID());
    }

    public static TournamentId of(String value) {
        return new TournamentId(UUID.fromString(value));
    }

    public static TournamentId of(UUID value) {
        return new TournamentId(value);
    }
}
