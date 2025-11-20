package com.tournamentapp.tournament_app.players.infrastructure.rest;

import com.tournamentapp.tournament_app.players.application.dto.PlayerResponse;
import com.tournamentapp.tournament_app.players.application.usecase.FindPlayerByIdUseCase;
import com.tournamentapp.tournament_app.players.application.usecase.FindPlayerByPersonIdUseCase;
import com.tournamentapp.tournament_app.players.application.usecase.GetAllPlayersUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/public/players")
public class PlayerPublicController {

    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final FindPlayerByPersonIdUseCase findPlayerByPersonIdUseCase;
    private final GetAllPlayersUseCase getAllPlayersUseCase;

    public PlayerPublicController(FindPlayerByIdUseCase findPlayerByIdUseCase,
                                  FindPlayerByPersonIdUseCase findPlayerByPersonIdUseCase,
                                  GetAllPlayersUseCase getAllPlayersUseCase) {
        this.findPlayerByIdUseCase = findPlayerByIdUseCase;
        this.findPlayerByPersonIdUseCase = findPlayerByPersonIdUseCase;
        this.getAllPlayersUseCase = getAllPlayersUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable UUID id) {
        PlayerResponse response = findPlayerByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PlayerResponse> getPlayerByPersonId(@PathVariable UUID personId) {
        PlayerResponse response = findPlayerByPersonIdUseCase.execute(personId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getAllPlayers() {
        List<PlayerResponse> response = getAllPlayersUseCase.execute();
        return ResponseEntity.ok(response);
    }
}
