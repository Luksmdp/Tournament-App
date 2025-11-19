package com.tournamentapp.tournament_app.people.application.usecase;

import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeletePersonUseCase {

    private final PersonRepository personRepository;

    public DeletePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public void execute(UUID id) {
        var person = personRepository.findById(new PersonId(id))
                .orElseThrow(() -> new PersonNotFoundException(id));

        personRepository.delete(person);
    }
}
