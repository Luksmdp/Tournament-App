package com.tournamentapp.tournament_app.people.infrastructure.rest;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.application.usecase.FindPersonByDniUseCase;
import com.tournamentapp.tournament_app.people.application.usecase.FindPersonByIdUseCase;
import com.tournamentapp.tournament_app.people.application.usecase.SearchPeopleUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/public/people")
public class PersonPublicController {

    private final FindPersonByIdUseCase findPersonByIdUseCase;
    private final FindPersonByDniUseCase findPersonByDniUseCase;
    private final SearchPeopleUseCase searchPeopleUseCase;

    public PersonPublicController(FindPersonByIdUseCase findPersonByIdUseCase,
                                  FindPersonByDniUseCase findPersonByDniUseCase,
                                  SearchPeopleUseCase searchPeopleUseCase) {
        this.findPersonByIdUseCase = findPersonByIdUseCase;
        this.findPersonByDniUseCase = findPersonByDniUseCase;
        this.searchPeopleUseCase = searchPeopleUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable UUID id) {
        PersonResponse response = findPersonByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PersonResponse> getPersonByDni(@PathVariable String dni) {
        PersonResponse response = findPersonByDniUseCase.execute(dni);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PersonResponse>> searchPeople(@RequestParam String q) {
        List<PersonResponse> response = searchPeopleUseCase.execute(q);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPeople() {
        List<PersonResponse> response = searchPeopleUseCase.executeAll();
        return ResponseEntity.ok(response);
    }
}
