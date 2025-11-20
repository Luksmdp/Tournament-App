package com.tournamentapp.tournament_app.players.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.domain.exception.PlayerNotFoundException;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FindPlayerByIdUseCase {

    private final PlayerRepository playerRepository;
    private final PersonRepository personRepository;

    public FindPlayerByIdUseCase(PlayerRepository playerRepository,
                                 PersonRepository personRepository) {
        this.playerRepository = playerRepository;
        this.personRepository = personRepository;
    }

    public PlayerResponse execute(UUID id) {
        var player = playerRepository.findById(new PlayerId(id))
                .orElseThrow(() -> new PlayerNotFoundException(id));

        // Cargar información de la persona
        var person = personRepository.findById(player.getPersonId())
                .orElseThrow(() -> new PersonNotFoundException(player.getPersonId().value()));

        PersonResponse personResponse = PersonResponse.from(person);

        return PlayerResponse.from(player, personResponse);
    }
}
