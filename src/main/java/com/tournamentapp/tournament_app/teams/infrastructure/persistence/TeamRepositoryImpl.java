package com.tournamentapp.tournament_app.teams.infrastructure.persistence;

import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.model.TeamStatus;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import com.tournamentapp.tournament_app.teams.infrastructure.mapper.TeamMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final TeamJpaRepository jpaRepository;
    private final TeamMapper mapper;

    public TeamRepositoryImpl(TeamJpaRepository jpaRepository, TeamMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Team save(Team team) {
        TeamEntity entity = mapper.toEntity(team);
        TeamEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Team> findById(TeamId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Team> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public List<Team> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Team> findByStatus(TeamStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Team> findByNameContaining(String searchTerm) {
        return jpaRepository.findByNameContaining(searchTerm).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void delete(Team team) {
        jpaRepository.deleteById(team.getId().value());
    }
}
