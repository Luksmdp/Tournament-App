package com.tournamentapp.tournament_app.players.infrastructure.rest;

import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.application.dto.RegisterPlayerRequest;
import com.tournamentapp.tournament_app.players.application.dto.UpdatePlayerProfileRequest;
import com.tournamentapp.tournament_app.players.application.usecase.DeletePlayerUseCase;
import com.tournamentapp.tournament_app.players.application.usecase.RegisterPlayerUseCase;
import com.tournamentapp.tournament_app.players.application.usecase.UpdatePlayerProfileUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/players")
@PreAuthorize("hasRole('ADMIN')")
public class PlayerController {

    private final RegisterPlayerUseCase registerPlayerUseCase;
    private final UpdatePlayerProfileUseCase updatePlayerProfileUseCase;
    private final DeletePlayerUseCase deletePlayerUseCase;

    public PlayerController(RegisterPlayerUseCase registerPlayerUseCase,
                            UpdatePlayerProfileUseCase updatePlayerProfileUseCase,
                            DeletePlayerUseCase deletePlayerUseCase) {
        this.registerPlayerUseCase = registerPlayerUseCase;
        this.updatePlayerProfileUseCase = updatePlayerProfileUseCase;
        this.deletePlayerUseCase = deletePlayerUseCase;
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> registerPlayer(
            @Valid @RequestBody RegisterPlayerRequest request) {
        PlayerResponse response = registerPlayerUseCase.execute(request);
        return ResponseEntity
                .created(URI.create("/api/v1/public/players/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<PlayerResponse> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePlayerProfileRequest request) {
        PlayerResponse response = updatePlayerProfileUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable UUID id) {
        deletePlayerUseCase.execute(id);
    }
}
