package com.tournamentapp.tournament_app.people.domain.model;

public record ContactInfo(String email, String phone, String address) {

    public ContactInfo {
        if (email != null && !email.isBlank() && !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}