package com.tournamentapp.tournament_app.teams.domain.model;

import com.tournamentapp.tournament_app.teams.domain.model.Team;
import com.tournamentapp.tournament_app.teams.domain.model.TeamStatus;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class TeamTest {

    @Test
    void shouldCreateTeamSuccessfully() {
        // Given
        String name = "Boca Juniors";
        String logoUrl = "http://example.com/boca.png";

        // When
        Team team = Team.create(name, logoUrl);

        // Then
        assertThat(team.getId()).isNotNull();
        assertThat(team.getName()).isEqualTo("Boca Juniors");
        assertThat(team.getLogoUrl()).isEqualTo(logoUrl);
        assertThat(team.getStatus()).isEqualTo(TeamStatus.ACTIVE);
        assertThat(team.isActive()).isTrue();
        assertThat(team.canParticipateInTournaments()).isTrue();
    }

    @Test
    void shouldNotCreateTeamWithBlankName() {
        // When & Then
        assertThatThrownBy(() -> Team.create("", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Team name cannot be blank");
    }

    @Test
    void shouldNotCreateTeamWithNullName() {
        // When & Then
        assertThatThrownBy(() -> Team.create(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Team name cannot be null");
    }

    @Test
    void shouldNotCreateTeamWithLongName() {
        // Given
        String longName = "A".repeat(201);

        // When & Then
        assertThatThrownBy(() -> Team.create(longName, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Team name cannot exceed 200 characters");
    }

    @Test
    void shouldUpdateTeamName() {
        // Given
        Team team = Team.create("River Plate", null);

        // When
        team.updateName("River Plate FC");

        // Then
        assertThat(team.getName()).isEqualTo("River Plate FC");
    }

    @Test
    void shouldUpdateLogoUrl() {
        // Given
        Team team = Team.create("Boca Juniors", null);

        // When
        team.updateLogoUrl("http://example.com/new-logo.png");

        // Then
        assertThat(team.getLogoUrl()).isEqualTo("http://example.com/new-logo.png");
    }

    @Test
    void shouldDeactivateTeam() {
        // Given
        Team team = Team.create("Boca Juniors", null);

        // When
        team.deactivate();

        // Then
        assertThat(team.getStatus()).isEqualTo(TeamStatus.INACTIVE);
        assertThat(team.isActive()).isFalse();
        assertThat(team.canParticipateInTournaments()).isFalse();
    }

    @Test
    void shouldActivateInactiveTeam() {
        // Given
        Team team = Team.create("Boca Juniors", null);
        team.deactivate();

        // When
        team.activate();

        // Then
        assertThat(team.getStatus()).isEqualTo(TeamStatus.ACTIVE);
        assertThat(team.isActive()).isTrue();
    }

    @Test
    void shouldDissolveTeam() {
        // Given
        Team team = Team.create("Boca Juniors", null);

        // When
        team.dissolve();

        // Then
        assertThat(team.getStatus()).isEqualTo(TeamStatus.DISSOLVED);
        assertThat(team.canParticipateInTournaments()).isFalse();
    }

    @Test
    void shouldNotActivateDissolvedTeam() {
        // Given
        Team team = Team.create("Boca Juniors", null);
        team.dissolve();

        // When & Then
        assertThatThrownBy(() -> team.activate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot activate a dissolved team");
    }

    @Test
    void shouldNotDeactivateDissolvedTeam() {
        // Given
        Team team = Team.create("Boca Juniors", null);
        team.dissolve();

        // When & Then
        assertThatThrownBy(() -> team.deactivate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot deactivate a dissolved team");
    }

    @Test
    void shouldTrimTeamName() {
        // Given
        String nameWithSpaces = "  Boca Juniors  ";

        // When
        Team team = Team.create(nameWithSpaces, null);

        // Then
        assertThat(team.getName()).isEqualTo("Boca Juniors");
    }
}
