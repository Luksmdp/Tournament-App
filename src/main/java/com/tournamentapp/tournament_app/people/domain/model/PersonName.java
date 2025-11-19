package com.tournamentapp.tournament_app.people.domain.model;

import java.util.Objects;

public record PersonName(String firstName, String lastName) {

    public PersonName {
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");

        if (firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
