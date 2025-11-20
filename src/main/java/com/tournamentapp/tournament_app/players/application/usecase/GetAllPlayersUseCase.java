package com.tournamentapp.tournament_app.players.application.usecase;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetAllPlayersUseCase {

    private final PlayerRepository playerRepository;
    private final PersonRepository personRepository;

    public GetAllPlayersUseCase(PlayerRepository playerRepository,
                                PersonRepository personRepository) {
        this.playerRepository = playerRepository;
        this.personRepository = personRepository;
    }

    public List<PlayerResponse> execute() {
        return playerRepository.findAll()
                .stream()
                .map(player -> {
                    var person = personRepository.findById(player.getPersonId());
                    return person
                            .map(p -> PlayerResponse.from(player, PersonResponse.from(p)))
                            .orElse(PlayerResponse.from(player));
                })
                .toList();
    }
}
