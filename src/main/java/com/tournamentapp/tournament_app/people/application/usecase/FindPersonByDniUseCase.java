package com.tournamentapp.tournament_app.people.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.model.Dni;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FindPersonByDniUseCase {

    private final PersonRepository personRepository;

    public FindPersonByDniUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse execute(String dni) {
        Dni dniValue = new Dni(dni);
        return personRepository.findByDni(dniValue)
                .map(PersonResponse::from)
                .orElseThrow(() -> new PersonNotFoundException(dni));
    }
}
