package com.tournamentapp.tournament_app.players.application.usecase;

import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.model.*;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import com.tournamentapp.tournament_app.players.application.dto.RegisterPlayerRequest;
import com.tournamentapp.tournament_app.players.domain.exception.PlayerAlreadyExistsException;
import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterPlayerUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PersonRepository personRepository;

    private RegisterPlayerUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterPlayerUseCase(playerRepository, personRepository);
    }

    @Test
    void shouldRegisterPlayerSuccessfully() {
        // Given
        UUID personId = UUID.randomUUID();
        RegisterPlayerRequest request = new RegisterPlayerRequest(
                personId,
                "El Tigre",
                "http://photo.com/1.jpg"
        );

        Person person = Person.create(
                new Dni("12345678"),
                new PersonName("Juan", "Pérez"),
                LocalDate.of(1990, 5, 15),
                Gender.MALE
        );

        when(personRepository.findById(any(PersonId.class))).thenReturn(Optional.of(person));
        when(playerRepository.existsByPersonId(any(PersonId.class))).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        // When
        var response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.personId()).isEqualTo(personId);
        assertThat(response.nickname()).isEqualTo("El Tigre");

        verify(personRepository).findById(any(PersonId.class));
        verify(playerRepository).existsByPersonId(any(PersonId.class));
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void shouldThrowExceptionWhenPersonDoesNotExist() {
        // Given
        RegisterPlayerRequest request = new RegisterPlayerRequest(
                UUID.randomUUID(),
                "El Tigre",
                null
        );

        when(personRepository.findById(any(PersonId.class))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(PersonNotFoundException.class);

        verify(personRepository).findById(any(PersonId.class));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void shouldThrowExceptionWhenPlayerAlreadyExists() {
        // Given
        UUID personId = UUID.randomUUID();
        RegisterPlayerRequest request = new RegisterPlayerRequest(
                personId,
                "El Tigre",
                null
        );

        Person person = Person.create(
                new Dni("12345678"),
                new PersonName("Juan", "Pérez"),
                LocalDate.of(1990, 5, 15),
                Gender.MALE
        );

        when(personRepository.findById(any(PersonId.class))).thenReturn(Optional.of(person));
        when(playerRepository.existsByPersonId(any(PersonId.class))).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(PlayerAlreadyExistsException.class)
                .hasMessageContaining(personId.toString());

        verify(playerRepository, never()).save(any(Player.class));
    }
}
