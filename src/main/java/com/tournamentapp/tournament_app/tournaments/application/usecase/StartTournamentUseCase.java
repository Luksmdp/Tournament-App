package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentResponse;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentNotFoundException;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class StartTournamentUseCase {

    private final TournamentRepository tournamentRepository;

    public StartTournamentUseCase(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    public TournamentResponse execute(UUID id) {
        Tournament tournament = tournamentRepository.findById(new TournamentId(id))
                .orElseThrow(() -> new TournamentNotFoundException(id));

        tournament.start(LocalDate.now());

        Tournament updated = tournamentRepository.save(tournament);

        // TODO: Publicar eventos
        updated.clearDomainEvents();

        return TournamentResponse.from(updated);
    }
}
