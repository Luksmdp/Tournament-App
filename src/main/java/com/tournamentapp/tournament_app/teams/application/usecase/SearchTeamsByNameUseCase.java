package com.tournamentapp.tournament_app.teams.application.usecase;

import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchTeamsByNameUseCase {

    private final TeamRepository teamRepository;

    public SearchTeamsByNameUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamResponse> execute(String searchTerm) {
        return teamRepository.findByNameContaining(searchTerm)
                .stream()
                .map(TeamResponse::from)
                .toList();
    }
}
