package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.players.domain.exception.PlayerNotFoundException;
import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentNotFoundException;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class AddPlayerToTournamentTeamUseCase {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public AddPlayerToTournamentTeamUseCase(TournamentRepository tournamentRepository,
                                            PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public void execute(UUID tournamentId, UUID teamId, UUID playerId, Integer shirtNumber) {
        // Verificar que el torneo existe
        Tournament tournament = tournamentRepository.findById(new TournamentId(tournamentId))
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        // Verificar que el jugador existe
        playerRepository.findById(new PlayerId(playerId))
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        // Agregar jugador al equipo en el torneo
        tournament.addPlayerToTeam(new TeamId(teamId), new PlayerId(playerId), shirtNumber);

        // Persistir
        tournamentRepository.save(tournament);
    }
}
