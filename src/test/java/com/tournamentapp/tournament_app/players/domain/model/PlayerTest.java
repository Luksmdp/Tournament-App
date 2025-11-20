package com.tournamentapp.tournament_app.players.domain.model;

import com.tournamentapp.tournament_app.people.domain.model.PersonId;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldCreatePlayerSuccessfully() {
        // Given
        PersonId personId = new PersonId(UUID.randomUUID());
        PlayerProfile profile = new PlayerProfile("El Tigre", null);

        // When
        Player player = Player.create(personId, profile);

        // Then
        assertThat(player.getId()).isNotNull();
        assertThat(player.getPersonId()).isEqualTo(personId);
        assertThat(player.getProfile().nickname()).isEqualTo("El Tigre");
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.ACTIVE);
        assertThat(player.isActive()).isTrue();
        assertThat(player.canPlay()).isTrue();
    }

    @Test
    void shouldCreatePlayerWithEmptyProfile() {
        // Given
        PersonId personId = new PersonId(UUID.randomUUID());

        // When
        Player player = Player.create(personId, PlayerProfile.empty());

        // Then
        assertThat(player.getProfile().nickname()).isNull();
        assertThat(player.getProfile().photoUrl()).isNull();
    }

    @Test
    void shouldUpdateProfile() {
        // Given
        PersonId personId = new PersonId(UUID.randomUUID());
        Player player = Player.create(personId, PlayerProfile.empty());

        // When
        PlayerProfile newProfile = new PlayerProfile("Nuevo Apodo", "http://photo.com/1.jpg");
        player.updateProfile(newProfile);

        // Then
        assertThat(player.getProfile().nickname()).isEqualTo("Nuevo Apodo");
        assertThat(player.getProfile().photoUrl()).isEqualTo("http://photo.com/1.jpg");
    }

    @Test
    void shouldSuspendPlayer() {
        // Given
        PersonId personId = new PersonId(UUID.randomUUID());
        Player player = Player.create(personId, PlayerProfile.empty());

        // When
        player.suspend();

        // Then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.SUSPENDED);
        assertThat(player.isActive()).isFalse();
        assertThat(player.canPlay()).isFalse();
    }

    @Test
    void shouldRetirePlayer() {
        // Given
        PersonId personId = new PersonId(UUID.randomUUID());
        Player player = Player.create(personId, PlayerProfile.empty());

        // When
        player.retire();

        // Then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.RETIRED);
        assertThat(player.canPlay()).isFalse();
    }

    @Test
    void shouldNotActivateRetiredPlayer() {
        // Given
        PersonId personId = new PersonId(UUID.randomUUID());
        Player player = Player.create(personId, PlayerProfile.empty());
        player.retire();

        // When & Then
        assertThatThrownBy(() -> player.activate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot activate a retired player");
    }

    @Test
    void shouldNotCreateProfileWithLongNickname() {
        // Given
        String longNickname = "A".repeat(51);

        // When & Then
        assertThatThrownBy(() -> new PlayerProfile(longNickname, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nickname cannot exceed 50 characters");
    }
}
