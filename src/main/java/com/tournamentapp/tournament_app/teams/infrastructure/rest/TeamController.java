package com.tournamentapp.tournament_app.teams.infrastructure.rest;

import com.tournamentapp.tournament_app.teams.application.dto.CreateTeamRequest;
import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.application.dto.UpdateTeamRequest;
import com.tournamentapp.tournament_app.teams.application.usecase.CreateTeamUseCase;
import com.tournamentapp.tournament_app.teams.application.usecase.DissolveTeamUseCase;
import com.tournamentapp.tournament_app.teams.application.usecase.UpdateTeamUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/teams")
@PreAuthorize("hasRole('ADMIN')")
public class TeamController {

    private final CreateTeamUseCase createTeamUseCase;
    private final UpdateTeamUseCase updateTeamUseCase;
    private final DissolveTeamUseCase dissolveTeamUseCase;

    public TeamController(CreateTeamUseCase createTeamUseCase,
                          UpdateTeamUseCase updateTeamUseCase,
                          DissolveTeamUseCase dissolveTeamUseCase) {
        this.createTeamUseCase = createTeamUseCase;
        this.updateTeamUseCase = updateTeamUseCase;
        this.dissolveTeamUseCase = dissolveTeamUseCase;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        TeamResponse response = createTeamUseCase.execute(request);
        return ResponseEntity
                .created(URI.create("/api/v1/public/teams/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTeamRequest request) {
        TeamResponse response = updateTeamUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dissolveTeam(@PathVariable UUID id) {
        dissolveTeamUseCase.execute(id);
    }
}
