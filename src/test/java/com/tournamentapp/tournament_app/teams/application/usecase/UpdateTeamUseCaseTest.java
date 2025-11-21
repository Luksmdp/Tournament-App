package com.tournamentapp.tournament_app.teams.application.usecase;

import com.tournamentapp.tournament_app.teams.application.dto.TeamResponse;
import com.tournamentapp.tournament_app.teams.application.dto.UpdateTeamRequest;
import com.tournamentapp.tournament_app.teams.application.usecase.UpdateTeamUseCase;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNameAlreadyExistsException;
import com.tournamentapp.tournament_app.teams.domain.exception.TeamNotFoundException;
import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.teams.domain.repository.TeamRepository;
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
class UpdateTeamUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    private UpdateTeamUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateTeamUseCase(teamRepository);
    }

    @Test
    void shouldUpdateTeamSuccessfully() {
        // Given
        UUID teamId = UUID.randomUUID();
        Team team = Team.create("Boca Juniors", null);
        UpdateTeamRequest request = new UpdateTeamRequest(
                "Boca Juniors FC",
                "http://example.com/new-logo.png"
        );

        when(teamRepository.findById(any(TeamId.class))).thenReturn(Optional.of(team));
        when(teamRepository.findByName("Boca Juniors FC")).thenReturn(Optional.empty());
        when(teamRepository.save(any(Team.class))).thenAnswer(i -> i.getArgument(0));

        // When
        TeamResponse response = useCase.execute(teamId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Boca Juniors FC");
        assertThat(response.logoUrl()).isEqualTo("http://example.com/new-logo.png");

        verify(teamRepository).findById(any(TeamId.class));
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void shouldThrowExceptionWhenTeamNotFound() {
        // Given
        UUID teamId = UUID.randomUUID();
        UpdateTeamRequest request = new UpdateTeamRequest("New Name", null);

        when(teamRepository.findById(any(TeamId.class))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(teamId, request))
                .isInstanceOf(TeamNotFoundException.class);

        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void shouldThrowExceptionWhenNewNameAlreadyExists() {
        // Given
        UUID teamId = UUID.randomUUID();
        Team team = Team.create("Boca Juniors", null);
        Team otherTeam = Team.create("River Plate", null);
        UpdateTeamRequest request = new UpdateTeamRequest("River Plate", null);

        when(teamRepository.findById(any(TeamId.class))).thenReturn(Optional.of(team));
        when(teamRepository.findByName("River Plate")).thenReturn(Optional.of(otherTeam));

        // When & Then
        assertThatThrownBy(() -> useCase.execute(teamId, request))
                .isInstanceOf(TeamNameAlreadyExistsException.class);

        verify(teamRepository, never()).save(any(Team.class));
    }
}
