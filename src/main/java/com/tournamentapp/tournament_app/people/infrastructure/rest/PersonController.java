package com.tournamentapp.tournament_app.people.infrastructure.rest;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.application.dto.RegisterPersonRequest;
import com.tournamentapp.tournament_app.people.application.dto.UpdatePersonRequest;
import com.tournamentapp.tournament_app.people.application.usecase.DeletePersonUseCase;
import com.tournamentapp.tournament_app.people.application.usecase.FindPersonByIdUseCase;
import com.tournamentapp.tournament_app.people.application.usecase.RegisterPersonUseCase;
import com.tournamentapp.tournament_app.people.application.usecase.UpdatePersonUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/people")
@PreAuthorize("hasRole('ADMIN')")
public class PersonController {

    private final RegisterPersonUseCase registerPersonUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;
    private final FindPersonByIdUseCase findPersonByIdUseCase;
    private final DeletePersonUseCase deletePersonUseCase;

    public PersonController(RegisterPersonUseCase registerPersonUseCase,
                            UpdatePersonUseCase updatePersonUseCase,
                            FindPersonByIdUseCase findPersonByIdUseCase,
                            DeletePersonUseCase deletePersonUseCase) {
        this.registerPersonUseCase = registerPersonUseCase;
        this.updatePersonUseCase = updatePersonUseCase;
        this.findPersonByIdUseCase = findPersonByIdUseCase;
        this.deletePersonUseCase = deletePersonUseCase;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> registerPerson(@Valid @RequestBody RegisterPersonRequest request) {
        PersonResponse response = registerPersonUseCase.execute(request);
        return ResponseEntity
                .created(URI.create("/api/v1/public/people/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePersonRequest request) {
        PersonResponse response = updatePersonUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable UUID id) {
        PersonResponse response = findPersonByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable UUID id) {
        deletePersonUseCase.execute(id);
    }
}
