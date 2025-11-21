package com.tournamentapp.tournament_app.tournaments.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface TournamentTeamJpaRepository extends JpaRepository<TournamentTeamEntity,UUID> {

    List<TournamentTeamEntity> findByTournamentId(UUID tournamentId);

    @Query("SELECT tt FROM TournamentTeamEntity tt LEFT JOIN FETCH tt.players WHERE tt.tournament.id = :tournamentId")
    List<TournamentTeamEntity> findByTournamentIdWithPlayers(@Param("tournamentId") UUID tournamentId);
}
