package com.tournamentapp.tournament_app.teams.domain.model;

import java.util.Objects;
import java.util.UUID;

public record TeamId(UUID value) {

    public TeamId {
        Objects.requireNonNull(value, "TeamId cannot be null");
    }

    public static TeamId generate() {
        return new TeamId(UUID.randomUUID());
    }

    public static TeamId of(String value) {
        return new TeamId(UUID.fromString(value));
    }

    public static TeamId of(UUID value) {
        return new TeamId(value);
    }
}
