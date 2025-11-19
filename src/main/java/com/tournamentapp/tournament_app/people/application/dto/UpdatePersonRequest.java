package com.tournamentapp.tournament_app.people.application.dto;

import com.tournamentapp.tournament_app.people.domain.model.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdatePersonRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 100)
        String lastName,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        Gender gender,

        @Email(message = "Invalid email format")
        String email,

        String phone,

        String address
) {}
