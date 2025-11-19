package com.tournamentapp.tournament_app.people.infrastructure.mapper;

import com.tournamentapp.tournament_app.people.domain.model.*;
import com.tournamentapp.tournament_app.people.infrastructure.persistence.PersonEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public Person toDomain(PersonEntity entity) {
        PersonId id = new PersonId(entity.getId());
        Dni dni = new Dni(entity.getDni());
        PersonName name = new PersonName(entity.getFirstName(), entity.getLastName());
        Gender gender = entity.getGender();

        ContactInfo contactInfo = null;
        if (entity.getEmail() != null || entity.getPhone() != null || entity.getAddress() != null) {
            contactInfo = new ContactInfo(entity.getEmail(), entity.getPhone(), entity.getAddress());
        }

        return Person.reconstruct(
                id,
                dni,
                name,
                entity.getDateOfBirth(),
                gender,
                contactInfo,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public PersonEntity toEntity(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.setId(person.getId().value());
        entity.setDni(person.getDni().value());
        entity.setFirstName(person.getName().firstName());
        entity.setLastName(person.getName().lastName());
        entity.setDateOfBirth(person.getDateOfBirth());
        entity.setGender(person.getGender());

        if (person.getContactInfo() != null) {
            entity.setEmail(person.getContactInfo().email());
            entity.setPhone(person.getContactInfo().phone());
            entity.setAddress(person.getContactInfo().address());
        }

        entity.setCreatedAt(person.getCreatedAt());
        entity.setUpdatedAt(person.getUpdatedAt());

        return entity;
    }
}
