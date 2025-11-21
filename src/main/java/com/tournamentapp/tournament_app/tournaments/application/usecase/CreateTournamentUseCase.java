package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.tournaments.application.dto.CreateTournamentRequest;
import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentResponse;
import com.tournamentapp.tournament_app.tournaments.domain.model.PointsRuleSet;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTournamentUseCase {

    private final TournamentRepository tournamentRepository;

    public CreateTournamentUseCase(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    public TournamentResponse execute(CreateTournamentRequest request) {
        // Crear reglas de puntos
        PointsRuleSet pointsRules = request.winPoints() != null
                ? new PointsRuleSet(
                request.winPoints(),
                request.drawPoints() != null ? request.drawPoints() : 1,
                request.lossPoints() != null ? request.lossPoints() : 0,
                true
        )
                : new PointsRuleSet(); // Valores por defecto

        // Crear torneo
        Tournament tournament = Tournament.create(
                request.name(),
                request.type(),
                request.year(),
                pointsRules
        );

        // Persistir
        Tournament savedTournament = tournamentRepository.save(tournament);

        // TODO: Publicar eventos
        savedTournament.clearDomainEvents();

        return TournamentResponse.from(savedTournament);
    }
}
