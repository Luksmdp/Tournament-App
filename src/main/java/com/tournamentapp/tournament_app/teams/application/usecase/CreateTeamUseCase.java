package com.tournamentapp.tournament_app.teams.application.usecase;

import com.tournamentapp.tournament_app.teams.application.dto.CreateTeamRequest;
import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNameAlreadyExistsException;
import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTeamUseCase {

    private final TeamRepository teamRepository;

    public CreateTeamUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public TeamResponse execute(CreateTeamRequest request) {
        // Validar que el nombre no exista
        if (teamRepository.existsByName(request.name())) {
            throw new TeamNameAlreadyExistsException(request.name());
        }

        // Crear equipo
        Team team = Team.create(request.name(), request.logoUrl());

        // Persistir
        Team savedTeam = teamRepository.save(team);

        // TODO: Publicar eventos de dominio
        // eventPublisher.publish(team.getDomainEvents());
        savedTeam.clearDomainEvents();

        return TeamResponse.from(savedTeam);
    }
}
