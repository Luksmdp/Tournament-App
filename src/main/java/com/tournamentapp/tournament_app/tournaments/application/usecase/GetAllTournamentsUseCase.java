package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentResponse;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetAllTournamentsUseCase {

    private final TournamentRepository tournamentRepository;

    public GetAllTournamentsUseCase(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public List<TournamentResponse> execute() {
        return tournamentRepository.findAll()
                .stream()
                .map(TournamentResponse::from)
                .toList();
    }
}
