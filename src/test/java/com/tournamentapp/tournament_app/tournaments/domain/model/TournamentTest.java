package com.tournamentapp.tournament_app.tournaments.domain.model;

import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.tournaments.domain.exception.InsufficientTeamsException;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TeamNotInTournamentException;
import com.tournamentapp.tournament_app.tournaments.domain.exception.TournamentAlreadyStartedException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class TournamentTest {

    @Test
    void shouldCreateTournamentSuccessfully() {
        // Given
        String name = "Liga 2025";
        TournamentType type = TournamentType.LEAGUE;
        Integer year = 2025;
        PointsRuleSet rules = new PointsRuleSet(3, 1, 0, true);

        // When
        Tournament tournament = Tournament.create(name, type, year, rules);

        // Then
        assertThat(tournament.getId()).isNotNull();
        assertThat(tournament.getName()).isEqualTo("Liga 2025");
        assertThat(tournament.getType()).isEqualTo(TournamentType.LEAGUE);
        assertThat(tournament.getYear()).isEqualTo(2025);
        assertThat(tournament.getStatus()).isEqualTo(TournamentStatus.CREATED);
        assertThat(tournament.getPointsRules()).isEqualTo(rules);
        assertThat(tournament.canModifyTeams()).isTrue();
    }

    @Test
    void shouldAddTeamToTournament() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId teamId = new TeamId(UUID.randomUUID());

        // When
        tournament.addTeam(teamId);

        // Then
        assertThat(tournament.getTeamsCount()).isEqualTo(1);
    }

    @Test
    void shouldNotAddDuplicateTeam() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId teamId = new TeamId(UUID.randomUUID());
        tournament.addTeam(teamId);

        // When & Then
        assertThatThrownBy(() -> tournament.addTeam(teamId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already added");
    }

    @Test
    void shouldAddPlayerToTeam() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId teamId = new TeamId(UUID.randomUUID());
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        tournament.addTeam(teamId);

        // When
        tournament.addPlayerToTeam(teamId, playerId, 10);

        // Then
        assertThat(tournament.getTeams()).hasSize(1);
        assertThat(tournament.getTeams().iterator().next().getPlayerCount()).isEqualTo(1);
    }

    @Test
    void shouldNotAddPlayerToNonExistentTeam() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId teamId = new TeamId(UUID.randomUUID());
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        // When & Then
        assertThatThrownBy(() -> tournament.addPlayerToTeam(teamId, playerId, 10))
                .isInstanceOf(TeamNotInTournamentException.class);
    }

    @Test
    void shouldChangePlayerTeam() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId team1Id = new TeamId(UUID.randomUUID());
        TeamId team2Id = new TeamId(UUID.randomUUID());
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        tournament.addTeam(team1Id);
        tournament.addTeam(team2Id);
        tournament.addPlayerToTeam(team1Id, playerId, 10);

        // When
        tournament.changePlayerTeam(playerId, team2Id);

        // Then
        TournamentTeam team1 = tournament.getTeams().stream()
                .filter(t -> t.getTeamId().equals(team1Id))
                .findFirst().orElseThrow();
        TournamentTeam team2 = tournament.getTeams().stream()
                .filter(t -> t.getTeamId().equals(team2Id))
                .findFirst().orElseThrow();

        assertThat(team1.getPlayerCount()).isEqualTo(0);
        assertThat(team2.getPlayerCount()).isEqualTo(1);
    }

    @Test
    void shouldStartTournamentWithMinimumTeams() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId team1 = new TeamId(UUID.randomUUID());
        TeamId team2 = new TeamId(UUID.randomUUID());

        tournament.addTeam(team1);
        tournament.addTeam(team2);

        // Agregar jugadores suficientes a cada equipo
        for (int i = 0; i < 7; i++) {
            tournament.addPlayerToTeam(team1, new PlayerId(UUID.randomUUID()), i + 1);
            tournament.addPlayerToTeam(team2, new PlayerId(UUID.randomUUID()), i + 1);
        }

        // When
        tournament.start(LocalDate.now());

        // Then
        assertThat(tournament.getStatus()).isEqualTo(TournamentStatus.IN_PROGRESS);
        assertThat(tournament.isInProgress()).isTrue();
        assertThat(tournament.canModifyTeams()).isFalse();
    }

    @Test
    void shouldNotStartTournamentWithInsufficientTeams() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId team1 = new TeamId(UUID.randomUUID());
        tournament.addTeam(team1);

        // When & Then
        assertThatThrownBy(() -> tournament.start(LocalDate.now()))
                .isInstanceOf(InsufficientTeamsException.class)
                .hasMessageContaining("at least 2 teams");
    }

    @Test
    void shouldNotStartTournamentWithTeamHavingInsufficientPlayers() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId team1 = new TeamId(UUID.randomUUID());
        TeamId team2 = new TeamId(UUID.randomUUID());

        tournament.addTeam(team1);
        tournament.addTeam(team2);

        // Solo 3 jugadores (necesita 7)
        for (int i = 0; i < 3; i++) {
            tournament.addPlayerToTeam(team1, new PlayerId(UUID.randomUUID()), i + 1);
        }

        // When & Then
        assertThatThrownBy(() -> tournament.start(LocalDate.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("does not have minimum");
    }

    @Test
    void shouldFinishTournament() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId team1 = new TeamId(UUID.randomUUID());
        TeamId team2 = new TeamId(UUID.randomUUID());

        tournament.addTeam(team1);
        tournament.addTeam(team2);

        for (int i = 0; i < 7; i++) {
            tournament.addPlayerToTeam(team1, new PlayerId(UUID.randomUUID()), i + 1);
            tournament.addPlayerToTeam(team2, new PlayerId(UUID.randomUUID()), i + 1);
        }

        tournament.start(LocalDate.now());

        // When
        tournament.finish(LocalDate.now());

        // Then
        assertThat(tournament.getStatus()).isEqualTo(TournamentStatus.FINISHED);
        assertThat(tournament.isFinished()).isTrue();
    }

    @Test
    void shouldNotAddTeamsAfterTournamentStarted() {
        // Given
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);
        TeamId team1 = new TeamId(UUID.randomUUID());
        TeamId team2 = new TeamId(UUID.randomUUID());
        TeamId team3 = new TeamId(UUID.randomUUID());

        tournament.addTeam(team1);
        tournament.addTeam(team2);

        for (int i = 0; i < 7; i++) {
            tournament.addPlayerToTeam(team1, new PlayerId(UUID.randomUUID()), i + 1);
            tournament.addPlayerToTeam(team2, new PlayerId(UUID.randomUUID()), i + 1);
        }

        tournament.start(LocalDate.now());

        // When & Then
        assertThatThrownBy(() -> tournament.addTeam(team3))
                .isInstanceOf(TournamentAlreadyStartedException.class)
                .hasMessageContaining("Cannot add teams once tournament has started");
    }

    @Test
    void shouldNotCreateTournamentWithInvalidYear() {
        // When & Then
        assertThatThrownBy(() -> Tournament.create("Liga 2025", TournamentType.LEAGUE, 2019, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Year must be between");
    }

    @Test
    void shouldUseDefaultPointsRulesWhenNull() {
        // Given & When
        Tournament tournament = Tournament.create("Liga 2025", TournamentType.LEAGUE, 2025, null);

        // Then
        assertThat(tournament.getPointsRules().winPoints()).isEqualTo(3);
        assertThat(tournament.getPointsRules().drawPoints()).isEqualTo(1);
        assertThat(tournament.getPointsRules().lossPoints()).isEqualTo(0);
        assertThat(tournament.getPointsRules().usesGoalDifference()).isTrue();
    }
}
