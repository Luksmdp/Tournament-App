package com.tournamentapp.tournament_app.tournaments.infrastructure.rest;

import com.tournamentapp.tournament_app.tournaments.application.dto.*;
import com.tournamentapp.tournament_app.tournaments.application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/tournaments")
@PreAuthorize("hasRole('ADMIN')")
public class TournamentController {

    private final CreateTournamentUseCase createTournamentUseCase;
    private final StartTournamentUseCase startTournamentUseCase;
    private final FinishTournamentUseCase finishTournamentUseCase;
    private final AddTeamToTournamentUseCase addTeamToTournamentUseCase;
    private final RemoveTeamFromTournamentUseCase removeTeamFromTournamentUseCase;
    private final AddPlayerToTournamentTeamUseCase addPlayerToTournamentTeamUseCase;
    private final RemovePlayerFromTournamentTeamUseCase removePlayerFromTournamentTeamUseCase;
    private final ChangePlayerTeamInTournamentUseCase changePlayerTeamInTournamentUseCase;

    public TournamentController(CreateTournamentUseCase createTournamentUseCase,
                                StartTournamentUseCase startTournamentUseCase,
                                FinishTournamentUseCase finishTournamentUseCase,
                                AddTeamToTournamentUseCase addTeamToTournamentUseCase,
                                RemoveTeamFromTournamentUseCase removeTeamFromTournamentUseCase,
                                AddPlayerToTournamentTeamUseCase addPlayerToTournamentTeamUseCase,
                                RemovePlayerFromTournamentTeamUseCase removePlayerFromTournamentTeamUseCase,
                                ChangePlayerTeamInTournamentUseCase changePlayerTeamInTournamentUseCase) {
        this.createTournamentUseCase = createTournamentUseCase;
        this.startTournamentUseCase = startTournamentUseCase;
        this.finishTournamentUseCase = finishTournamentUseCase;
        this.addTeamToTournamentUseCase = addTeamToTournamentUseCase;
        this.removeTeamFromTournamentUseCase = removeTeamFromTournamentUseCase;
        this.addPlayerToTournamentTeamUseCase = addPlayerToTournamentTeamUseCase;
        this.removePlayerFromTournamentTeamUseCase = removePlayerFromTournamentTeamUseCase;
        this.changePlayerTeamInTournamentUseCase = changePlayerTeamInTournamentUseCase;
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(
            @Valid @RequestBody CreateTournamentRequest request) {
        TournamentResponse response = createTournamentUseCase.execute(request);
        return ResponseEntity
                .created(URI.create("/api/v1/public/tournaments/" + response.id()))
                .body(response);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TournamentResponse> startTournament(@PathVariable UUID id) {
        TournamentResponse response = startTournamentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<TournamentResponse> finishTournament(@PathVariable UUID id) {
        TournamentResponse response = finishTournamentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tournamentId}/teams")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTeamToTournament(
            @PathVariable UUID tournamentId,
            @Valid @RequestBody AddTeamToTournamentRequest request) {
        addTeamToTournamentUseCase.execute(tournamentId, request.teamId());
    }

    @DeleteMapping("/{tournamentId}/teams/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTeamFromTournament(
            @PathVariable UUID tournamentId,
            @PathVariable UUID teamId) {
        removeTeamFromTournamentUseCase.execute(tournamentId, teamId);
    }

    @PostMapping("/{tournamentId}/teams/{teamId}/players")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPlayerToTournamentTeam(
            @PathVariable UUID tournamentId,
            @PathVariable UUID teamId,
            @Valid @RequestBody AddPlayerToTournamentTeamRequest request) {
        addPlayerToTournamentTeamUseCase.execute(
                tournamentId,
                teamId,
                request.playerId(),
                request.shirtNumber()
        );
    }

    @DeleteMapping("/{tournamentId}/teams/{teamId}/players/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePlayerFromTournamentTeam(
            @PathVariable UUID tournamentId,
            @PathVariable UUID teamId,
            @PathVariable UUID playerId) {
        removePlayerFromTournamentTeamUseCase.execute(tournamentId, teamId, playerId);
    }

    @PutMapping("/{tournamentId}/players/{playerId}/team")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePlayerTeam(
            @PathVariable UUID tournamentId,
            @PathVariable UUID playerId,
            @Valid @RequestBody ChangePlayerTeamRequest request) {
        changePlayerTeamInTournamentUseCase.execute(tournamentId, playerId, request.newTeamId());
    }
}
