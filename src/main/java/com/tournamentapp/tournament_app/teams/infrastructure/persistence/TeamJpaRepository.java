package com.tournamentapp.tournament_app.teams.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamJpaRepository extends JpaRepository<TeamEntity,UUID> {

    Optional<TeamEntity> findByName(String name);

    boolean existsByName(String name);

    List<TeamEntity> findByStatus(String status);

    @Query("SELECT t FROM TeamEntity t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<TeamEntity> findByNameContaining(@Param("searchTerm") String searchTerm);
}
