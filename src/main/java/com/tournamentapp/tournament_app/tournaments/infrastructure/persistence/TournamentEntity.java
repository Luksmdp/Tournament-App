package com.tournamentapp.tournament_app.tournaments.infrastructure.persistence;

import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentStatus;
import com.tournamentapp.tournament_app.tournaments.domain.model.TournamentType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tournaments")
public class TournamentEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tournament_type", nullable = false, length = 50)
    private TournamentType tournamentType;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TournamentStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "win_points", nullable = false)
    private Integer winPoints;

    @Column(name = "draw_points", nullable = false)
    private Integer drawPoints;

    @Column(name = "loss_points", nullable = false)
    private Integer lossPoints;

    @Column(name = "uses_goal_difference", nullable = false)
    private Boolean usesGoalDifference;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TournamentTeamEntity> teams = new HashSet<>();

    // Constructors
    public TournamentEntity() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public TournamentType getTournamentType() { return tournamentType; }
    public void setTournamentType(TournamentType tournamentType) { this.tournamentType = tournamentType; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public TournamentStatus getStatus() { return status; }
    public void setStatus(TournamentStatus status) { this.status = status; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getWinPoints() { return winPoints; }
    public void setWinPoints(Integer winPoints) { this.winPoints = winPoints; }

    public Integer getDrawPoints() { return drawPoints; }
    public void setDrawPoints(Integer drawPoints) { this.drawPoints = drawPoints; }

    public Integer getLossPoints() { return lossPoints; }
    public void setLossPoints(Integer lossPoints) { this.lossPoints = lossPoints; }

    public Boolean getUsesGoalDifference() { return usesGoalDifference; }
    public void setUsesGoalDifference(Boolean usesGoalDifference) { this.usesGoalDifference = usesGoalDifference; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<TournamentTeamEntity> getTeams() { return teams; }
    public void setTeams(Set<TournamentTeamEntity> teams) { this.teams = teams; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
