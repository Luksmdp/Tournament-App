package com.tournamentapp.tournament_app.tournaments.domain.repository;

import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentStatus;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository {

    Tournament save(Tournament tournament);

    Optional<Tournament> findById(TournamentId id);

    List<Tournament> findAll();

    List<Tournament> findByStatus(TournamentStatus status);

    List<Tournament> findByYear(Integer year);

    List<Tournament> findByNameContaining(String searchTerm);

    void delete(Tournament tournament);
}
