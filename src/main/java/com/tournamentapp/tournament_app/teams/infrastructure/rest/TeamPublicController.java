package com.tournamentapp.tournament_app.teams.infrastructure.rest;

import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.application.usecase.FindTeamByIdUseCase;
import com.tournamentapp.tournament_app.teams.application.usecase.GetAllTeamsUseCase;
import com.tournamentapp.tournament_app.teams.application.usecase.SearchTeamsByNameUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/public/teams")
public class TeamPublicController {

    private final FindTeamByIdUseCase findTeamByIdUseCase;
    private final GetAllTeamsUseCase getAllTeamsUseCase;
    private final SearchTeamsByNameUseCase searchTeamsByNameUseCase;

    public TeamPublicController(FindTeamByIdUseCase findTeamByIdUseCase,
                                GetAllTeamsUseCase getAllTeamsUseCase,
                                SearchTeamsByNameUseCase searchTeamsByNameUseCase) {
        this.findTeamByIdUseCase = findTeamByIdUseCase;
        this.getAllTeamsUseCase = getAllTeamsUseCase;
        this.searchTeamsByNameUseCase = searchTeamsByNameUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable UUID id) {
        TeamResponse response = findTeamByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        List<TeamResponse> response = getAllTeamsUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamResponse>> searchTeams(@RequestParam String q) {
        List<TeamResponse> response = searchTeamsByNameUseCase.execute(q);
        return ResponseEntity.ok(response);
    }
}
