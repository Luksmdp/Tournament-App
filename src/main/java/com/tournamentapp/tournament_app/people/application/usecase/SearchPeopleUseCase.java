package com.tournamentapp.tournament_app.people.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchPeopleUseCase {

    private final PersonRepository personRepository;

    public SearchPeopleUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonResponse> execute(String searchTerm) {
        return personRepository.findByNameContaining(searchTerm)
                .stream()
                .map(PersonResponse::from)
                .toList();
    }

    public List<PersonResponse> executeAll() {
        return personRepository.findAll()
                .stream()
                .map(PersonResponse::from)
                .toList();
    }
}
