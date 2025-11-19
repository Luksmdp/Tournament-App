package com.tournamentapp.tournament_app.people.domain.model;

import com.tournamentapp.tournament_app.people.domain.exception.InvalidDniException;

import java.util.Objects;
import java.util.regex.Pattern;

public record Dni(String value) {

    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{7,8}$");

    public Dni {
        Objects.requireNonNull(value, "DNI cannot be null");
        if (!isValid(value)) {
            throw new InvalidDniException("DNI must have 7 or 8 digits: " + value);
        }
    }

    private boolean isValid(String value) {
        return DNI_PATTERN.matcher(value.trim()).matches();
    }

    public String formatted() {
        String cleanDni = value.trim();
        if (cleanDni.length() == 7) {
            return String.format("%s.%s.%s",
                    cleanDni.substring(0, 1),
                    cleanDni.substring(1, 4),
                    cleanDni.substring(4));
        } else {
            return String.format("%s.%s.%s",
                    cleanDni.substring(0, 2),
                    cleanDni.substring(2, 5),
                    cleanDni.substring(5));
        }
    }
}
