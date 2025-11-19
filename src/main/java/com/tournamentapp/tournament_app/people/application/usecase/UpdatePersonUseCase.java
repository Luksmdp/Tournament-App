package com.tournamentapp.tournament_app.people.application.usecase;
import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.application.dto.UpdatePersonRequest;
import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.model.ContactInfo;
import com.tournamentapp.tournament_app.people.domain.model.Person;
import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.people.domain.model.PersonName;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class UpdatePersonUseCase {

    private final PersonRepository personRepository;

    public UpdatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public PersonResponse execute(UUID id, UpdatePersonRequest request) {
        // Buscar persona
        Person person = personRepository.findById(new PersonId(id))
                .orElseThrow(() -> new PersonNotFoundException(id));

        // Actualizar datos
        PersonName newName = new PersonName(request.firstName(), request.lastName());
        person.updateName(newName);
        person.updateDateOfBirth(request.dateOfBirth());
        person.updateGender(request.gender());

        // Actualizar información de contacto
        ContactInfo newContactInfo = new ContactInfo(
                request.email(),
                request.phone(),
                request.address()
        );
        person.updateContactInfo(newContactInfo);

        // Persistir
        Person updatedPerson = personRepository.save(person);

        return PersonResponse.from(updatedPerson);
    }
}
