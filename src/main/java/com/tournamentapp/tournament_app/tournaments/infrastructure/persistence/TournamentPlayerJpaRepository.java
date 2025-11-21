package com.tournamentapp.tournament_app.tournaments.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TournamentPlayerJpaRepository extends JpaRepository<TournamentPlayerEntity,UUID> {

    List<TournamentPlayerEntity> findByTournamentId(UUID tournamentId);

    List<TournamentPlayerEntity> findByTournamentTeamId(UUID tournamentTeamId);
}
