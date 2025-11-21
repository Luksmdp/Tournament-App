package com.tournamentapp.tournament_app.tournaments.infrastructure.persistence;

import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentStatus;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import com.tournamentapp.tournament_app.tournaments.infrastructure.mapper.TournamentMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class TournamentRepositoryImpl implements TournamentRepository {

    private final TournamentJpaRepository jpaRepository;
    private final TournamentMapper mapper;

    public TournamentRepositoryImpl(TournamentJpaRepository jpaRepository, TournamentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Tournament save(Tournament tournament) {
        TournamentEntity entity = mapper.toEntity(tournament);
        TournamentEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Tournament> findById(TournamentId id) {
        return jpaRepository.findByIdWithTeams(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Tournament> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Tournament> findByStatus(TournamentStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Tournament> findByYear(Integer year) {
        return jpaRepository.findByYear(year).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Tournament> findByNameContaining(String searchTerm) {
        return jpaRepository.findByNameContaining(searchTerm).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Tournament tournament) {
        jpaRepository.deleteById(tournament.getId().value());
    }
}
