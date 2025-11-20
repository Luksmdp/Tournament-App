package com.tournamentapp.tournament_app.players.domain.model;

public record PlayerProfile(String nickname, String photoUrl) {

    public PlayerProfile {
        if (nickname != null && nickname.length() > 50) {
            throw new IllegalArgumentException("Nickname cannot exceed 50 characters");
        }
        if (photoUrl != null && photoUrl.length() > 500) {
            throw new IllegalArgumentException("Photo URL cannot exceed 500 characters");
        }
    }

    public static PlayerProfile empty() {
        return new PlayerProfile(null, null);
    }
}
