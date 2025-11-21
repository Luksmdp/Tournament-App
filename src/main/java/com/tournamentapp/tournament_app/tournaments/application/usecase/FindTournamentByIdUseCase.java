package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentResponse;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentNotFoundException;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FindTournamentByIdUseCase {

    private final TournamentRepository tournamentRepository;

    public FindTournamentByIdUseCase(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public TournamentResponse execute(UUID id) {
        Tournament tournament = tournamentRepository.findById(new TournamentId(id))
                .orElseThrow(() -> new TournamentNotFoundException(id));

        return TournamentResponse.from(tournament);
    }
}
