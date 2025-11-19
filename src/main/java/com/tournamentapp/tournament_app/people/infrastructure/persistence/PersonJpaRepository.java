package com.tournamentapp.tournament_app.people.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonJpaRepository extends JpaRepository<PersonEntity,UUID> {

    Optional<PersonEntity> findByDni(String dni);

    boolean existsByDni(String dni);

    @Query("SELECT p FROM PersonEntity p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PersonEntity> findByNameContaining(@Param("searchTerm") String searchTerm);
}
