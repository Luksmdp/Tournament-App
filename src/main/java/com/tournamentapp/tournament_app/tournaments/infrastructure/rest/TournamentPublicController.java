package com.tournamentapp.tournament_app.tournaments.infrastructure.rest;

import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentResponse;
import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentTeamResponse;
import com.tournamentapp.tournament_app.tournaments.application.usecase.FindTournamentByIdUseCase;
import com.tournamentapp.tournament_app.tournaments.application.usecase.GetAllTournamentsUseCase;
import com.tournamentapp.tournament_app.tournaments.application.usecase.GetTournamentTeamsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/public/tournaments")
public class TournamentPublicController {

    private final FindTournamentByIdUseCase findTournamentByIdUseCase;
    private final GetAllTournamentsUseCase getAllTournamentsUseCase;
    private final GetTournamentTeamsUseCase getTournamentTeamsUseCase;

    public TournamentPublicController(FindTournamentByIdUseCase findTournamentByIdUseCase,
                                      GetAllTournamentsUseCase getAllTournamentsUseCase,
                                      GetTournamentTeamsUseCase getTournamentTeamsUseCase) {
        this.findTournamentByIdUseCase = findTournamentByIdUseCase;
        this.getAllTournamentsUseCase = getAllTournamentsUseCase;
        this.getTournamentTeamsUseCase = getTournamentTeamsUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getTournamentById(@PathVariable UUID id) {
        TournamentResponse response = findTournamentByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TournamentResponse>> getAllTournaments() {
        List<TournamentResponse> response = getAllTournamentsUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<List<TournamentTeamResponse>> getTournamentTeams(@PathVariable UUID id) {
        List<TournamentTeamResponse> response = getTournamentTeamsUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
