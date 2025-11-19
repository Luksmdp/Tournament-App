package com.tournamentapp.tournament_app.people.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FindPersonByIdUseCase {

    private final PersonRepository personRepository;

    public FindPersonByIdUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse execute(UUID id) {
        return personRepository.findById(new PersonId(id))
                .map(PersonResponse::from)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
