package com.tournamentapp.tournament_app.tournaments.application.usecase;

import com.tournamentapp.tournament_app.tournaments.application.dto.CreateTournamentRequest;
import com.tournamentapp.tournament_app.tournaments.application.dto.TournamentResponse;
import com.tournamentapp.tournament_app.tournaments.domain.model.Tournament;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentType;
import com.tournamentapp.tournament_app.tournaments.domain.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTournamentUseCaseTest {

    @Mock
    private TournamentRepository tournamentRepository;

    private CreateTournamentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateTournamentUseCase(tournamentRepository);
    }

    @Test
    void shouldCreateTournamentSuccessfully() {
        // Given
        CreateTournamentRequest request = new CreateTournamentRequest(
                "Liga 2025",
                TournamentType.LEAGUE,
                2025,
                3, 1, 0
        );

        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(i -> i.getArgument(0));

        // When
        TournamentResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Liga 2025");
        assertThat(response.type()).isEqualTo(TournamentType.LEAGUE);
        assertThat(response.year()).isEqualTo(2025);
        assertThat(response.pointsRules().winPoints()).isEqualTo(3);

        verify(tournamentRepository).save(any(Tournament.class));
    }

    @Test
    void shouldCreateTournamentWithDefaultPointsRules() {
        // Given
        CreateTournamentRequest request = new CreateTournamentRequest(
                "Liga 2025",
                TournamentType.LEAGUE,
                2025,
                null, null, null
        );

        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(i -> i.getArgument(0));

        // When
        TournamentResponse response = useCase.execute(request);

        // Then
        assertThat(response.pointsRules().winPoints()).isEqualTo(3);
        assertThat(response.pointsRules().drawPoints()).isEqualTo(1);
        assertThat(response.pointsRules().lossPoints()).isEqualTo(0);
    }
}
