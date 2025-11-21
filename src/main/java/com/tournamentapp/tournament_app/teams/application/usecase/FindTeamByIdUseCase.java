package com.tournamentapp.tournament_app.teams.application.usecase;

import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNotFoundException;
import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FindTeamByIdUseCase {

    private final TeamRepository teamRepository;

    public FindTeamByIdUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public TeamResponse execute(UUID id) {
        Team team = teamRepository.findById(new TeamId(id))
                .orElseThrow(() -> new TeamNotFoundException(id));

        return TeamResponse.from(team);
    }
}
