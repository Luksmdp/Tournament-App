package com.tournamentapp.tournament_app.people.infrastructure.persistence;

import com.tournamentapp.tournament_app.people.domain.model.Dni;
import com.tournamentapp.tournament_app.people.domain.model.Person;
import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import com.tournamentapp.tournament_app.people.infrastructure.mapper.PersonMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final PersonJpaRepository jpaRepository;
    private final PersonMapper mapper;

    public PersonRepositoryImpl(PersonJpaRepository jpaRepository, PersonMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Person save(Person person) {
        PersonEntity entity = mapper.toEntity(person);
        PersonEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Person> findById(PersonId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Person> findByDni(Dni dni) {
        return jpaRepository.findByDni(dni.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Person> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Person> findByNameContaining(String searchTerm) {
        return jpaRepository.findByNameContaining(searchTerm).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByDni(Dni dni) {
        return jpaRepository.existsByDni(dni.value());
    }

    @Override
    public void delete(Person person) {
        jpaRepository.deleteById(person.getId().value());
    }
}
