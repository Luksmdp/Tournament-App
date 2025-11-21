package com.tournamentapp.tournament_app.tournaments.domain.model;

import com.tournamentapp.tournament_app.players.domain.model.PlayerId;
import com.tournamentapp.tournament_app.shared.domain.AggregateRoot;
import com.tournamentapp.tournament_app.teams.domain.model.TeamId;
import com.tournamentapp.tournament_app.tournaments.domain.event.*;
import com.tournamentapp.tournament_app.tournaments.domain.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Tournament extends AggregateRoot {

    private final TournamentId id;
    private String name;
    private final TournamentType type;
    private final Integer year;
    private TournamentStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private PointsRuleSet pointsRules;
    private final Set<TournamentTeam> teams;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Configuración
    private static final int MIN_TEAMS_TO_START = 2;
    private static final int MIN_PLAYERS_PER_TEAM = 7;

    // Constructor privado
    private Tournament(TournamentId id, String name, TournamentType type, Integer year,
                       PointsRuleSet pointsRules) {
        this.id = Objects.requireNonNull(id, "TournamentId cannot be null");
        this.name = validateName(name);
        this.type = Objects.requireNonNull(type, "TournamentType cannot be null");
        this.year = validateYear(year);
        this.pointsRules = pointsRules != null ? pointsRules : new PointsRuleSet();
        this.status = TournamentStatus.CREATED;
        this.teams = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        registerEvent(new TournamentCreated(id.value(), name, type, year));
    }

    // Factory method para crear nuevo torneo
    public static Tournament create(String name, TournamentType type, Integer year, PointsRuleSet pointsRules) {
        return new Tournament(TournamentId.generate(), name, type, year, pointsRules);
    }

    // Factory method para reconstruir desde BD
    public static Tournament reconstruct(TournamentId id, String name, TournamentType type, Integer year,
                                         TournamentStatus status, LocalDate startDate, LocalDate endDate,
                                         PointsRuleSet pointsRules, Set<TournamentTeam> teams,
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        Tournament tournament = new Tournament(id, name, type, year, pointsRules);
        tournament.status = status;
        tournament.startDate = startDate;
        tournament.endDate = endDate;
        tournament.teams.addAll(teams);
        tournament.updatedAt = updatedAt;
        tournament.clearDomainEvents();
        return tournament;
    }

    // ==================== GESTIÓN DE EQUIPOS ====================

    public void addTeam(TeamId teamId) {
        if (!canModifyTeams()) {
            throw new TournamentAlreadyStartedException("Cannot add teams once tournament has started");
        }

        if (teams.stream().anyMatch(t -> t.getTeamId().equals(teamId))) {
            throw new IllegalArgumentException("Team already added to tournament");
        }

        TournamentTeam tournamentTeam = TournamentTeam.create(id, teamId);
        teams.add(tournamentTeam);
        this.updatedAt = LocalDateTime.now();

        registerEvent(new TeamAddedToTournament(id.value(), teamId.value()));
    }

    public void removeTeam(TeamId teamId) {
        if (!canModifyTeams()) {
            throw new TournamentAlreadyStartedException("Cannot remove teams once tournament has started");
        }

        teams.removeIf(t -> t.getTeamId().equals(teamId));
        this.updatedAt = LocalDateTime.now();
    }

    // ==================== GESTIÓN DE JUGADORES ====================

    public void addPlayerToTeam(TeamId teamId, PlayerId playerId, Integer shirtNumber) {
        if (!canModifyTeams()) {
            throw new TournamentAlreadyStartedException("Cannot add players once tournament has started");
        }

        TournamentTeam team = findTeamById(teamId);

        // Verificar que el jugador no esté en otro equipo del torneo
        Optional<TournamentTeam> existingTeam = findTeamWithPlayer(playerId);
        if (existingTeam.isPresent() && !existingTeam.get().getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("Player is already in another team in this tournament");
        }

        TournamentPlayer player = TournamentPlayer.create(id, teamId, playerId, shirtNumber);
        team.addPlayer(player);
        this.updatedAt = LocalDateTime.now();

        registerEvent(new PlayerAddedToTournamentTeam(id.value(), teamId.value(), playerId.value()));
    }

    public void removePlayerFromTeam(TeamId teamId, PlayerId playerId) {
        if (!canModifyTeams()) {
            throw new TournamentAlreadyStartedException("Cannot remove players once tournament has started");
        }

        TournamentTeam team = findTeamById(teamId);
        team.removePlayer(playerId);
        this.updatedAt = LocalDateTime.now();
    }

    public void changePlayerTeam(PlayerId playerId, TeamId newTeamId) {
        if (!canModifyTeams()) {
            throw new TournamentAlreadyStartedException("Cannot change player team once tournament has started");
        }

        // Buscar equipo actual del jugador
        TournamentTeam currentTeam = findTeamWithPlayer(playerId)
                .orElseThrow(() -> new PlayerNotInTournamentException(playerId.value()));

        // Verificar que el nuevo equipo existe
        TournamentTeam newTeam = findTeamById(newTeamId);

        // Obtener jugador del equipo actual
        TournamentPlayer player = currentTeam.getPlayers().stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new PlayerNotInTournamentException(playerId.value()));

        // Cambiar de equipo
        player.changeTeam(newTeamId);

        // Remover del equipo actual y agregar al nuevo
        currentTeam.removePlayer(playerId);
        newTeam.addPlayer(player);

        this.updatedAt = LocalDateTime.now();
    }

    // ==================== CICLO DE VIDA DEL TORNEO ====================

    public void start(LocalDate startDate) {
        if (status != TournamentStatus.CREATED) {
            throw new InvalidTournamentStateException("Tournament can only be started from CREATED status");
        }

        if (teams.size() < MIN_TEAMS_TO_START) {
            throw new InsufficientTeamsException(
                    String.format("Need at least %d teams to start tournament, but only %d registered",
                            MIN_TEAMS_TO_START, teams.size())
            );
        }

        // Verificar que todos los equipos tengan el mínimo de jugadores
        for (TournamentTeam team : teams) {
            if (!team.hasMinimumPlayers(MIN_PLAYERS_PER_TEAM)) {
                throw new IllegalStateException(
                        String.format("Team %s does not have minimum %d players",
                                team.getTeamId().value(), MIN_PLAYERS_PER_TEAM)
                );
            }
        }

        this.status = TournamentStatus.IN_PROGRESS;
        this.startDate = startDate;
        this.updatedAt = LocalDateTime.now();

        registerEvent(new TournamentStarted(id.value(), startDate));
    }

    public void finish(LocalDate endDate) {
        if (status != TournamentStatus.IN_PROGRESS) {
            throw new InvalidTournamentStateException("Tournament must be IN_PROGRESS to finish");
        }

        this.status = TournamentStatus.FINISHED;
        this.endDate = endDate;
        this.updatedAt = LocalDateTime.now();

        registerEvent(new TournamentFinished(id.value(), endDate));
    }

    // ==================== MÉTODOS DE CONSULTA ====================

    public boolean canModifyTeams() {
        return status == TournamentStatus.CREATED;
    }

    public boolean isInProgress() {
        return status == TournamentStatus.IN_PROGRESS;
    }

    public boolean isFinished() {
        return status == TournamentStatus.FINISHED;
    }

    private TournamentTeam findTeamById(TeamId teamId) {
        return teams.stream()
                .filter(t -> t.getTeamId().equals(teamId))
                .findFirst()
                .orElseThrow(() -> new TeamNotInTournamentException(teamId.value()));
    }

    private Optional<TournamentTeam> findTeamWithPlayer(PlayerId playerId) {
        return teams.stream()
                .filter(team -> team.getPlayers().stream()
                        .anyMatch(player -> player.getPlayerId().equals(playerId)))
                .findFirst();
    }

    // ==================== VALIDACIONES ====================

    private String validateName(String name) {
        Objects.requireNonNull(name, "Tournament name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Tournament name cannot be blank");
        }
        if (name.length() > 200) {
            throw new IllegalArgumentException("Tournament name cannot exceed 200 characters");
        }
        return name.trim();
    }

    private Integer validateYear(Integer year) {
        Objects.requireNonNull(year, "Year cannot be null");
        int currentYear = LocalDate.now().getYear();
        if (year < 2020 || year > currentYear + 10) {
            throw new IllegalArgumentException("Year must be between 2020 and " + (currentYear + 10));
        }
        return year;
    }

    // ==================== GETTERS ====================

    public TournamentId getId() { return id; }
    public String getName() { return name; }
    public TournamentType getType() { return type; }
    public Integer getYear() { return year; }
    public TournamentStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public PointsRuleSet getPointsRules() { return pointsRules; }
    public Set<TournamentTeam> getTeams() { return Collections.unmodifiableSet(teams); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public int getTeamsCount() { return teams.size(); }
}
