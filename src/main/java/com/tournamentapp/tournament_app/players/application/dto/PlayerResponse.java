package com.tournamentapp.tournament_app.players.application.dto;

import com.tournamentapp.tournament_app.people.application.dto.PersonResponse;
import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.model.PlayerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerResponse(
        UUID id,
        UUID personId,
        String nickname,
        String photoUrl,
        PlayerStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PersonResponse person  // Información de la persona asociada
) {
    // Constructor sin PersonResponse (para casos donde no se carga la persona)
    public PlayerResponse(UUID id, UUID personId, String nickname, String photoUrl,
                          PlayerStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(id, personId, nickname, photoUrl, status, createdAt, updatedAt, null);
    }

    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
                player.getId().value(),
                player.getPersonId().value(),
                player.getProfile().nickname(),
                player.getProfile().photoUrl(),
                player.getStatus(),
                player.getCreatedAt(),
                player.getUpdatedAt()
        );
    }

    public static PlayerResponse from(Player player, PersonResponse person) {
        return new PlayerResponse(
                player.getId().value(),
                player.getPersonId().value(),
                player.getProfile().nickname(),
                player.getProfile().photoUrl(),
                player.getStatus(),
                player.getCreatedAt(),
                player.getUpdatedAt(),
                person
        );
    }
}
