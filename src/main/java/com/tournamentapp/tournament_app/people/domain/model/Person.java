package com.tournamentapp.tournament_app.people.domain.model;

import com.tournamentapp.tournament_app.people.domain.event.PersonRegistered;
import com.tournamentapp.tournament_app.shared.domain.AggregateRoot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;

public class Person extends AggregateRoot {

    private final PersonId id;
    private final Dni dni;
    private PersonName name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private ContactInfo contactInfo;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor privado - usar factory method
    private Person(PersonId id, Dni dni, PersonName name, LocalDate dateOfBirth, Gender gender) {
        this.id = Objects.requireNonNull(id, "PersonId cannot be null");
        this.dni = Objects.requireNonNull(dni, "DNI cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "Date of birth cannot be null");
        this.gender = gender;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Registrar evento de dominio
        registerEvent(new PersonRegistered(id.value(), dni.value(), name.fullName()));
    }

    // Factory method para crear nueva persona
    public static Person create(Dni dni, PersonName name, LocalDate dateOfBirth, Gender gender) {
        return new Person(PersonId.generate(), dni, name, dateOfBirth, gender);
    }

    // Factory method para reconstruir desde BD
    public static Person reconstruct(PersonId id, Dni dni, PersonName name, LocalDate dateOfBirth,
                                     Gender gender, ContactInfo contactInfo,
                                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        Person person = new Person(id, dni, name, dateOfBirth, gender);
        person.contactInfo = contactInfo;
        person.updatedAt = updatedAt;
        return person;
    }

    // Métodos de negocio
    public void updateName(PersonName newName) {
        this.name = Objects.requireNonNull(newName, "Name cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDateOfBirth(LocalDate newDateOfBirth) {
        this.dateOfBirth = Objects.requireNonNull(newDateOfBirth, "Date of birth cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void updateGender(Gender newGender) {
        this.gender = newGender;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContactInfo(ContactInfo newContactInfo) {
        this.contactInfo = newContactInfo;
        this.updatedAt = LocalDateTime.now();
    }

    public int calculateAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public boolean isAdult() {
        return calculateAge() >= 18;
    }

    // Getters
    public PersonId getId() { return id; }
    public Dni getDni() { return dni; }
    public PersonName getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public ContactInfo getContactInfo() { return contactInfo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
