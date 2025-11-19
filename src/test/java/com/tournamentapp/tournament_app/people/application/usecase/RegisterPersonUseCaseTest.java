package com.tournamentapp.tournament_app.people.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.application.dto.RegisterPersonRequest;
import com.tournamentapp.tournament_app.people.domain.exception.DniAlreadyExistsException;
import com.tournamentapp.tournament_app.people.domain.model.Dni;
import com.tournamentapp.tournament_app.people.domain.model.Gender;
import com.tournamentapp.tournament_app.people.domain.model.Person;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterPersonUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    private RegisterPersonUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterPersonUseCase(personRepository);
    }

    @Test
    void shouldRegisterPersonSuccessfully() {
        // Given
        RegisterPersonRequest request = new RegisterPersonRequest(
                "12345678",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 5, 15),
                Gender.MALE,
                "juan@example.com",
                "2234567890",
                "Calle 123"
        );

        when(personRepository.existsByDni(any(Dni.class))).thenReturn(false);
        when(personRepository.save(any(Person.class))).thenAnswer(i -> i.getArgument(0));

        // When
        PersonResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.dni()).isEqualTo("12345678");
        assertThat(response.fullName()).isEqualTo("Juan Pérez");
        assertThat(response.email()).isEqualTo("juan@example.com");

        verify(personRepository).existsByDni(any(Dni.class));
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void shouldThrowExceptionWhenDniAlreadyExists() {
        // Given
        RegisterPersonRequest request = new RegisterPersonRequest(
                "12345678",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 5, 15),
                Gender.MALE,
                null,
                null,
                null
        );

        when(personRepository.existsByDni(any(Dni.class))).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(DniAlreadyExistsException.class)
                .hasMessageContaining("12345678");

        verify(personRepository).existsByDni(any(Dni.class));
        verify(personRepository, never()).save(any(Person.class));
    }
}