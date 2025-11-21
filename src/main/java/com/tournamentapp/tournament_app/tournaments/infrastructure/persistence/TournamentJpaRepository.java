package com.tournamentapp.tournament_app.tournaments.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentJpaRepository extends JpaRepository<TournamentEntity,UUID> {

    List<TournamentEntity> findByStatus(String status);

    List<TournamentEntity> findByYear(Integer year);

    @Query("SELECT t FROM TournamentEntity t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<TournamentEntity> findByNameContaining(@Param("searchTerm") String searchTerm);

    @Query("SELECT t FROM TournamentEntity t LEFT JOIN FETCH t.teams WHERE t.id = :id")
    Optional<TournamentEntity> findByIdWithTeams(@Param("id") UUID id);
}
