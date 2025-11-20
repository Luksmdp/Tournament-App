package com.tournamentapp.tournament_app.players.domain.model;

import java.util.Objects;
import java.util.UUID;

public record PlayerId(UUID value) {

    public PlayerId {
        Objects.requireNonNull(value, "PlayerId cannot be null");
    }

    public static PlayerId generate() {
        return new PlayerId(UUID.randomUUID());
    }

    public static PlayerId of(String value) {
        return new PlayerId(UUID.fromString(value));
    }

    public static PlayerId of(UUID value) {
        return new PlayerId(value);
    }
}
