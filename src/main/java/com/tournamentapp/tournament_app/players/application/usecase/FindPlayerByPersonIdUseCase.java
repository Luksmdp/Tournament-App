package com.tournamentapp.tournament_app.players.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.domain.exception.PlayerNotFoundException;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FindPlayerByPersonIdUseCase {

    private final PlayerRepository playerRepository;
    private final PersonRepository personRepository;

    public FindPlayerByPersonIdUseCase(PlayerRepository playerRepository,
                                       PersonRepository personRepository) {
        this.playerRepository = playerRepository;
        this.personRepository = personRepository;
    }

    public PlayerResponse execute(UUID personId) {
        PersonId pid = new PersonId(personId);

        var player = playerRepository.findByPersonId(pid)
                .orElseThrow(() -> new PlayerNotFoundException(
                        "No player found for person with id: " + personId));

        var person = personRepository.findById(pid)
                .orElseThrow(() -> new PersonNotFoundException(personId));

        PersonResponse personResponse = PersonResponse.from(person);

        return PlayerResponse.from(player, personResponse);
    }
}
