package com.tournamentapp.tournament_app.players.infrastructure.mapper;

import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.players.domain.model.PlayerProfile;
import com.tournamentapp.tournament_app.players.domain.model.PlayerStatus;
import com.tournamentapp.tournament_app.players.infrastructure.persistence.PlayerEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Player toDomain(PlayerEntity entity) {
        PlayerId id = new PlayerId(entity.getId());
        PersonId personId = new PersonId(entity.getPersonId());
        PlayerProfile profile = new PlayerProfile(entity.getNickname(), entity.getPhotoUrl());
        PlayerStatus status = entity.getStatus();

        return Player.reconstruct(
                id,
                personId,
                profile,
                status,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public PlayerEntity toEntity(Player player) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(player.getId().value());
        entity.setPersonId(player.getPersonId().value());
        entity.setNickname(player.getProfile().nickname());
        entity.setPhotoUrl(player.getProfile().photoUrl());
        entity.setStatus(player.getStatus());
        entity.setCreatedAt(player.getCreatedAt());
        entity.setUpdatedAt(player.getUpdatedAt());

        return entity;
    }
}
