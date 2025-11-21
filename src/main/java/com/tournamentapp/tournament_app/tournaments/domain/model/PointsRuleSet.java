package com.tournamentapp.tournament_app.tournaments.domain.model;


public record PointsRuleSet(
        int winPoints,
        int drawPoints,
        int lossPoints,
        boolean usesGoalDifference
) {

    // Constructor con valores por defecto
    public PointsRuleSet() {
        this(3, 1, 0, true);
    }

    public PointsRuleSet {
        if (winPoints < 0 || drawPoints < 0 || lossPoints < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        if (winPoints <= drawPoints) {
            throw new IllegalArgumentException("Win points must be greater than draw points");
        }
    }
}
