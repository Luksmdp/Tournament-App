package com.tournamentapp.tournament_app.players.application.usecase;

import com.tournamentapp.tournament_app.people.domain.exception.PersonNotFoundException;
import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.people.domain.repository.PersonRepository;
import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.application.dto.RegisterPlayerRequest;
import com.tournamentapp.tournament_app.players.domain.exception.PlayerAlreadyExistsException;
import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.model.PlayerProfile;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterPlayerUseCase {

    private final PlayerRepository playerRepository;
    private final PersonRepository personRepository;

    public RegisterPlayerUseCase(PlayerRepository playerRepository,
                                 PersonRepository personRepository) {
        this.playerRepository = playerRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public PlayerResponse execute(RegisterPlayerRequest request) {
        PersonId personId = new PersonId(request.personId());

        // Verificar que la persona existe
        personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(request.personId()));

        // Verificar que no existe ya un jugador para esta persona
        if (playerRepository.existsByPersonId(personId)) {
            throw new PlayerAlreadyExistsException(request.personId());
        }

        // Crear jugador
        PlayerProfile profile = new PlayerProfile(request.nickname(), request.photoUrl());
        Player player = Player.create(personId, profile);

        // Persistir
        Player savedPlayer = playerRepository.save(player);

        // TODO: Publicar eventos de dominio
        // eventPublisher.publish(player.getDomainEvents());
        savedPlayer.clearDomainEvents();

        return PlayerResponse.from(savedPlayer);
    }
}
