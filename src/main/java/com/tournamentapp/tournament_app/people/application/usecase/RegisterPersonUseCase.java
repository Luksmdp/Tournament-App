package com.tournamentapp.tournament_app.people.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.application.dto.RegisterPersonRequest;
import com.tournamentapp.tournament_app.people.domain.exception.DniAlreadyExistsException;
import com.tournamentapp.tournament_app.people.domain.model.ContactInfo;
import com.tournamentapp.tournament_app.people.domain.model.Dni;
import com.tournamentapp.tournament_app.people.domain.model.Person;
import com.tournamentapp.tournament_app.people.domain.model.PersonName;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterPersonUseCase {

    private final PersonRepository personRepository;

    public RegisterPersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public PersonResponse execute(RegisterPersonRequest request) {
        // Validar que el DNI no exista
        Dni dni = new Dni(request.dni());
        if (personRepository.existsByDni(dni)) {
            throw new DniAlreadyExistsException(request.dni());
        }

        // Crear persona
        PersonName name = new PersonName(request.firstName(), request.lastName());
        Person person = Person.create(dni, name, request.dateOfBirth(), request.gender());

        // Agregar información de contacto si existe
        if (hasContactInfo(request)) {
            ContactInfo contactInfo = new ContactInfo(
                    request.email(),
                    request.phone(),
                    request.address()
            );
            person.updateContactInfo(contactInfo);
        }

        // Persistir
        Person savedPerson = personRepository.save(person);

        // TODO: Publicar eventos de dominio
        // eventPublisher.publish(person.getDomainEvents());
        person.clearDomainEvents();

        return PersonResponse.from(savedPerson);
    }

    private boolean hasContactInfo(RegisterPersonRequest request) {
        return request.email() != null || request.phone() != null || request.address() != null;
    }
}
