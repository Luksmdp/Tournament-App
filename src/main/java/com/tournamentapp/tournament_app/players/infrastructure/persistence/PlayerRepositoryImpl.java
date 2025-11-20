package com.tournamentapp.tournament_app.players.infrastructure.persistence;

import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import com.tournamentapp.tournament_app.players.domain.model.Player;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.players.domain.model.PlayerStatus;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import com.tournamentapp.tournament_app.players.infrastructure.mapper.PlayerMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private final PlayerJpaRepository jpaRepository;
    private final PlayerMapper mapper;

    public PlayerRepositoryImpl(PlayerJpaRepository jpaRepository, PlayerMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Player save(Player player) {
        PlayerEntity entity = mapper.toEntity(player);
        PlayerEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Player> findById(PlayerId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Player> findByPersonId(PersonId personId) {
        return jpaRepository.findByPersonId(personId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Player> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Player> findByStatus(PlayerStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByPersonId(PersonId personId) {
        return jpaRepository.existsByPersonId(personId.value());
    }

    @Override
    public void delete(Player player) {
        jpaRepository.deleteById(player.getId().value());
    }
}
