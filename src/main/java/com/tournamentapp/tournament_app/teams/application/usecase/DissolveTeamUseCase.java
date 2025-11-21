package com.tournamentapp.tournament_app.teams.application.usecase;

import com.tournamentapp.tournament_app.teams.domain.exception.TeamNotFoundException;
import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class DissolveTeamUseCase {

    private final TeamRepository teamRepository;

    public DissolveTeamUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void execute(UUID id) {
        Team team = teamRepository.findById(new TeamId(id))
                .orElseThrow(() -> new TeamNotFoundException(id));

        team.dissolve();

        teamRepository.save(team);

        // TODO: Publicar eventos de dominio
        // eventPublisher.publish(team.getDomainEvents());
        team.clearDomainEvents();
    }
}
