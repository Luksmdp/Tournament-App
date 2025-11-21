package com.tournamentapp.tournament_app.teams.application.usecase;

import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.application.dto.UpdateTeamRequest;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNameAlreadyExistsException;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNotFoundException;
import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateTeamUseCase {

    private final TeamRepository teamRepository;

    public UpdateTeamUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public TeamResponse execute(UUID id, UpdateTeamRequest request) {
        // Buscar equipo
        Team team = teamRepository.findById(new TeamId(id))
                .orElseThrow(() -> new TeamNotFoundException(id));

        // Validar que el nuevo nombre no exista (si cambió)
        if (!team.getName().equals(request.name())) {
            Optional<Team> existingTeam = teamRepository.findByName(request.name());
            if (existingTeam.isPresent() && !existingTeam.get().getId().equals(team.getId())) {
                throw new TeamNameAlreadyExistsException(request.name());
            }
        }

        // Actualizar datos
        team.updateName(request.name());
        team.updateLogoUrl(request.logoUrl());

        // Persistir
        Team updatedTeam = teamRepository.save(team);

        return TeamResponse.from(updatedTeam);
    }
}