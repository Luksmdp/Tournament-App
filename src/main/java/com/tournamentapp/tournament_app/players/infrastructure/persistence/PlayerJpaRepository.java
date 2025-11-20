package com.tournamentapp.tournament_app.players.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity,UUID> {

    Optional<PlayerEntity> findByPersonId(UUID personId);

    boolean existsByPersonId(UUID personId);

    List<PlayerEntity> findByStatus(String status);
}
