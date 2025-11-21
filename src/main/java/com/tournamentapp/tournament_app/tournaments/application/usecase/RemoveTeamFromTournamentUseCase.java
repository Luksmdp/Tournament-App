package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentNotFoundException;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class RemoveTeamFromTournamentUseCase {

    private final TournamentRepository tournamentRepository;

    public RemoveTeamFromTournamentUseCase(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    public void execute(UUID tournamentId, UUID teamId) {
        Tournament tournament = tournamentRepository.findById(new TournamentId(tournamentId))
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        tournament.removeTeam(new TeamId(teamId));

        tournamentRepository.save(tournament);
    }
}
