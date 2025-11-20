package com.tournamentapp.tournament_app.players.application.usecase;

import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.application.dto.UpdatePlayerProfileRequest;
import com.tournamentapp.tournament_app.players.domain.exception.PlayerNotFoundException;
import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.players.domain.model.PlayerProfile;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class UpdatePlayerProfileUseCase {

    private final PlayerRepository playerRepository;

    public UpdatePlayerProfileUseCase(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public PlayerResponse execute(UUID id, UpdatePlayerProfileRequest request) {
        // Buscar jugador
        Player player = playerRepository.findById(new PlayerId(id))
                .orElseThrow(() -> new PlayerNotFoundException(id));

        // Actualizar perfil
        PlayerProfile newProfile = new PlayerProfile(request.nickname(), request.photoUrl());
        player.updateProfile(newProfile);

        // Persistir
        Player updatedPlayer = playerRepository.save(player);

        return PlayerResponse.from(updatedPlayer);
    }
}
