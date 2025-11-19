package com.tournamentapp.tournament_app.people.domain.model;

import java.util.Objects;
import java.util.UUID;

public record PersonId(UUID value) {

    public PersonId {
        Objects.requireNonNull(value, "PersonId cannot be null");
    }

    public static PersonId generate() {
        return new PersonId(UUID.randomUUID());
    }

    public static PersonId of(String value) {
        return new PersonId(UUID.fromString(value));
    }
}