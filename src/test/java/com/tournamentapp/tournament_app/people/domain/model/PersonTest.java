package com.tournamentapp.tournament_app.people.domain.model;

import com.tournamentapp.tournament_app.people.domain.exception.InvalidDniException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonTest {

    @Test
    void shouldCreatePersonSuccessfully() {
        // Given
        Dni dni = new Dni("12345678");
        PersonName name = new PersonName("Juan", "Pérez");
        LocalDate birthDate = LocalDate.of(1990, 5, 15);

        // When
        Person person = Person.create(dni, name, birthDate, Gender.MALE);

        // Then
        assertThat(person.getId()).isNotNull();
        assertThat(person.getDni().value()).isEqualTo("12345678");
        assertThat(person.getName().fullName()).isEqualTo("Juan Pérez");
        assertThat(person.calculateAge()).isGreaterThan(30);
        assertThat(person.isAdult()).isTrue();
    }

    @Test
    void shouldNotCreatePersonWithInvalidDni() {
        // Given
        String invalidDni = "123";

        // When & Then
        assertThatThrownBy(() -> new Dni(invalidDni))
                .isInstanceOf(InvalidDniException.class)
                .hasMessageContaining("DNI must have 7 or 8 digits");
    }

    @Test
    void shouldFormatDniCorrectly() {
        // Given
        Dni dni7 = new Dni("1234567");
        Dni dni8 = new Dni("12345678");

        // When & Then
        assertThat(dni7.formatted()).isEqualTo("1.234.567");
        assertThat(dni8.formatted()).isEqualTo("12.345.678");
    }

    @Test
    void shouldUpdatePersonName() {
        // Given
        Person person = Person.create(
                new Dni("12345678"),
                new PersonName("Juan", "Pérez"),
                LocalDate.of(1990, 5, 15),
                Gender.MALE
        );

        // When
        PersonName newName = new PersonName("Carlos", "González");
        person.updateName(newName);

        // Then
        assertThat(person.getName().fullName()).isEqualTo("Carlos González");
    }

    @Test
    void shouldUpdateContactInfo() {
        // Given
        Person person = Person.create(
                new Dni("12345678"),
                new PersonName("Juan", "Pérez"),
                LocalDate.of(1990, 5, 15),
                Gender.MALE
        );

        // When
        ContactInfo contactInfo = new ContactInfo(
                "juan@example.com",
                "2234567890",
                "Calle 123"
        );
        person.updateContactInfo(contactInfo);

        // Then
        assertThat(person.getContactInfo()).isNotNull();
        assertThat(person.getContactInfo().email()).isEqualTo("juan@example.com");
    }

    @Test
    void shouldCalculateAgeCorrectly() {
        // Given
        LocalDate birthDate = LocalDate.now().minusYears(25).minusDays(1);
        Person person = Person.create(
                new Dni("12345678"),
                new PersonName("Juan", "Pérez"),
                birthDate,
                Gender.MALE
        );

        // When
        int age = person.calculateAge();

        // Then
        assertThat(age).isEqualTo(25);
    }
}
