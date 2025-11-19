package com.tournamentapp.tournament_app.people.domain.repository;

import com.tournamentapp.tournament_app.people.domain.model.Dni;
import com.tournamentapp.tournament_app.people.domain.model.Person;
import com.tournamentapp.tournament_app.people.domain.model.PersonId;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(PersonId id);

    Optional<Person> findByDni(Dni dni);

    List<Person> findAll();

    List<Person> findByNameContaining(String searchTerm);

    boolean existsByDni(Dni dni);

    void delete(Person person);
}
