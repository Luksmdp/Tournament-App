package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.teams.domain.exception.TeamNotFoundException;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentNotFoundException;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class AddTeamToTournamentUseCase {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    public AddTeamToTournamentUseCase(TournamentRepository tournamentRepository,
                                      TeamRepository teamRepository) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void execute(UUID tournamentId, UUID teamId) {
        // Verificar que el torneo existe
        Tournament tournament = tournamentRepository.findById(new TournamentId(tournamentId))
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        // Verificar que el equipo existe
        teamRepository.findById(new TeamId(teamId))
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        // Agregar equipo al torneo
        tournament.addTeam(new TeamId(teamId));

        // Persistir
        tournamentRepository.save(tournament);
    }
}
