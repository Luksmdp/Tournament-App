package com.tournamentapp.tournament_app.players.application.usecase;

import com.tournamentapp.tournament_app.players.domain.exception.PlayerNotFoundException;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class DeletePlayerUseCase {

    private final PlayerRepository playerRepository;

    public DeletePlayerUseCase(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public void execute(UUID id) {
        var player = playerRepository.findById(new PlayerId(id))
                .orElseThrow(() -> new PlayerNotFoundException(id));

        playerRepository.delete(player);
    }
}
