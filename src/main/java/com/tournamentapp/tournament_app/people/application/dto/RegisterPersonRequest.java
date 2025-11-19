package com.tournamentapp.tournament_app.people.application.dto;

import com.tournamentapp.tournament_app.people.domain.model.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterPersonRequest(
        @NotBlank(message = "DNI is required")
        @Pattern(regexp = "\\d{7,8}", message = "DNI must have 7 or 8 digits")
        String dni,

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
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
