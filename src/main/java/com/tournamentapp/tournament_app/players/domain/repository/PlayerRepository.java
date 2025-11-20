package com.tournamentapp.tournament_app.players.domain.repository;

import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.players.domain.model.PlayerStatus;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {

    Player save(Player player);

    Optional<Player> findById(PlayerId id);

    Optional<Player> findByPersonId(PersonId personId);

    List<Player> findAll();

    List<Player> findByStatus(PlayerStatus status);

    boolean existsByPersonId(PersonId personId);

    void delete(Player player);
}
