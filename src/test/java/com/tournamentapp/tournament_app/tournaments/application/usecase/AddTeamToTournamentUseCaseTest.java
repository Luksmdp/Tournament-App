package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.teams.domain.exception.TeamNotFoundException;
import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentNotFoundException;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentId;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentType;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddTeamToTournamentUseCaseTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TeamRepository teamRepository;

    private AddTeamToTournamentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddTeamToTournamentUseCase(tournamentRepository, teamRepository);
    }

    @Test
    void shouldAddTeamToTournamentSuccessfully() {
        // Given
        UUID tournamentId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        Team team = Team.create("Boca Juniors", null);

        when(tournamentRepository.findById(any(TournamentId.class))).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(any(TeamId.class))).thenReturn(Optional.of(team));
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(i -> i.getArgument(0));

        // When
        useCase.execute(tournamentId, teamId);

        // Then
        verify(tournamentRepository).findById(any(TournamentId.class));
        verify(teamRepository).findById(any(TeamId.class));
        verify(tournamentRepository).save(any(Tournament.class));
    }

    @Test
    void shouldThrowExceptionWhenTournamentNotFound() {
        // Given
        UUID tournamentId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        when(tournamentRepository.findById(any(TournamentId.class))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(tournamentId, teamId))
                .isInstanceOf(TournamentNotFoundException.class);

        verify(teamRepository, never()).findById(any(TeamId.class));
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    @Test
    void shouldThrowExceptionWhenTeamNotFound() {
        // Given
        UUID tournamentId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);

        when(tournamentRepository.findById(any(TournamentId.class))).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(any(TeamId.class))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(tournamentId, teamId))
                .isInstanceOf(TeamNotFoundException.class);

        verify(tournamentRepository, never()).save(any(Tournament.class));
    }
}
