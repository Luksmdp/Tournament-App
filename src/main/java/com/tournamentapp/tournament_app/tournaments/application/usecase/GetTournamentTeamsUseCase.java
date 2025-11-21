package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.domain.repository.PlayerRepository;
import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentPlayerResponse;
import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentTeamResponse;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentNotFoundException;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetTournamentTeamsUseCase {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public GetTournamentTeamsUseCase(TournamentRepository tournamentRepository,
                                     TeamRepository teamRepository,
                                     PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    public List<TournamentTeamResponse> execute(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(new TournamentId(tournamentId))
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        return tournament.getTeams().stream()
                .map(tournamentTeam -> {
                    // Cargar información del equipo
                    TeamResponse team = teamRepository.findById(tournamentTeam.getTeamId())
                            .map(TeamResponse::from)
                            .orElse(null);

                    // Cargar información de los jugadores
                    List<TournamentPlayerResponse> players = tournamentTeam.getPlayers().stream()
                            .map(tournamentPlayer -> {
                                PlayerResponse player = playerRepository.findById(tournamentPlayer.getPlayerId())
                                        .map(PlayerResponse::from)
                                        .orElse(null);

                                return new TournamentPlayerResponse(
                                        tournamentPlayer.getId(),
                                        tournamentPlayer.getTournamentId().value(),
                                        tournamentPlayer.getTeamId().value(),
                                        tournamentPlayer.getPlayerId().value(),
                                        player,
                                        tournamentPlayer.getShirtNumber(),
                                        tournamentPlayer.getAddedAt()
                                );
                            })
                            .toList();

                    return new TournamentTeamResponse(
                            tournamentTeam.getId(),
                            tournamentTeam.getTournamentId().value(),
                            tournamentTeam.getTeamId().value(),
                            team,
                            tournamentTeam.isActive(),
                            tournamentTeam.getPlayerCount(),
                            players,
                            tournamentTeam.getAddedAt()
                    );
                })
                .toList();
    }
}
