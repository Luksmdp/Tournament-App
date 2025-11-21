package com.tournamentapp.tournament_app.teams.domain.repository;

import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.model.TeamStatus;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    Team save(Team team);

    Optional<Team> findById(TeamId id);

    Optional<Team> findByName(String name);

    List<Team> findAll();

    List<Team> findByStatus(TeamStatus status);

    List<Team> findByNameContaining(String searchTerm);

    boolean existsByName(String name);

    void delete(Team team);
}
