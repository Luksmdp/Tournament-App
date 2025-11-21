package com.tournamentapp.tournament_app.teams.application.usecase;

import com.tournamentapp.tournament_app.teams.application.dto.CreateTeamRequest;
import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.application.usecase.CreateTeamUseCase;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNameAlreadyExistsException;
import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTeamUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    private CreateTeamUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateTeamUseCase(teamRepository);
    }

    @Test
    void shouldCreateTeamSuccessfully() {
        // Given
        CreateTeamRequest request = new CreateTeamRequest(
                "Boca Juniors",
                "http://example.com/boca.png"
        );

        when(teamRepository.existsByName(anyString())).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenAnswer(i -> i.getArgument(0));

        // When
        TeamResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Boca Juniors");
        assertThat(response.logoUrl()).isEqualTo("http://example.com/boca.png");
        assertThat(response.status()).isEqualTo(com.tournamentapp.tournament_app.teams.domain.model.TeamStatus.ACTIVE);

        verify(teamRepository).existsByName("Boca Juniors");
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void shouldThrowExceptionWhenTeamNameAlreadyExists() {
        // Given
        CreateTeamRequest request = new CreateTeamRequest(
                "Boca Juniors",
                null
        );

        when(teamRepository.existsByName("Boca Juniors")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(TeamNameAlreadyExistsException.class)
                .hasMessageContaining("Boca Juniors");

        verify(teamRepository).existsByName("Boca Juniors");
        verify(teamRepository, never()).save(any(Team.class));
    }
}