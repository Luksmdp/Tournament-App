package com.tournamentapp.tournament_app.people.application.dto;

import com.tournamentapp.tournament_app.people.domain.model.Gender;
import com.tournamentapp.tournament_app.people.domain.model.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PersonResponse(
        UUID id,
        String dni,
        String dniFormatted,
        String firstName,
        String lastName,
        String fullName,
        LocalDate dateOfBirth,
        Integer age,
        Gender gender,
        String email,
        String phone,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId().value(),
                person.getDni().value(),
                person.getDni().formatted(),
                person.getName().firstName(),
                person.getName().lastName(),
                person.getName().fullName(),
                person.getDateOfBirth(),
                person.calculateAge(),
                person.getGender(),
                person.getContactInfo() != null ? person.getContactInfo().email() : null,
                person.getContactInfo() != null ? person.getContactInfo().phone() : null,
                person.getContactInfo() != null ? person.getContactInfo().address() : null,
                person.getCreatedAt(),
                person.getUpdatedAt()
        );
    }
}
