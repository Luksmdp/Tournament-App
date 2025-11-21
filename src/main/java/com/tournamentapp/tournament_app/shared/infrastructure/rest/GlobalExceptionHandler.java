package com.tournamentapp.tournament_app.shared.infrastructure.rest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tournamentapp.tournament_app.people.domain.exception.DniAlreadyExistsException;
import com.tournamentapp.tournament_app.people.domain.exception.InvalidDniException;
import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.players.domain.exception.PersonNotEligibleException;
import com.tournamentapp.tournament_app.players.domain.exception.PlayerAlreadyExistsException;
import com.tournamentapp.tournament_app.players.domain.exception.PlayerNotFoundException;
import com.tournamentapp.tournament_app.players.domain.model.PlayerStatus;
import com.tournamentapp.tournament_app.shared.domain.DomainException;
import com.tournamentapp.tournament_app.shared.domain.ErrorResponse;
import com.tournamentapp.tournament_app.shared.domain.ValidationErrorResponse;
import com.tournamentapp.tournament_app.teams.domain.exception.InvalidTeamException;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNameAlreadyExistsException;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Person
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotFound(PersonNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Person not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DniAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDniAlreadyExists(DniAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "DNI already exists",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidDniException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDni(InvalidDniException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid DNI",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Domain error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                "An unexpected error occurred",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(
            InvalidFormatException ex) {

        String fieldName = ex.getPath().isEmpty() ? "unknown"
                : ex.getPath().get(0).getFieldName();
        Object invalidValue = ex.getValue();

        String message;
        if (ex.getTargetType().isEnum()) {
            @SuppressWarnings("unchecked")
            Class<? extends Enum<PlayerStatus>> enumClass = (Class<? extends Enum<PlayerStatus>>) ex.getTargetType();
            String validValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            message = String.format(
                    "Invalid value '%s' for field '%s'. Valid values are: [%s]",
                    invalidValue, fieldName, validValues
            );
        } else {
            message = String.format(
                    "Invalid format for field '%s': %s",
                    fieldName, invalidValue
            );
        }

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid field value",
                message,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja errores de JSON mal formado
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        // Si la causa es un InvalidFormatException, delegamos
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) cause);
        }

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON",
                "The request body contains invalid JSON format",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //Player

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotFound(PlayerNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Player not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePlayerAlreadyExists(PlayerAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Player already exists",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(PersonNotEligibleException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotEligible(PersonNotEligibleException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Person not eligible",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //Team

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFound(TeamNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Team not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TeamNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTeamNameAlreadyExists(TeamNameAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Team name already exists",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidTeamException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTeam(InvalidTeamException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid team",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

