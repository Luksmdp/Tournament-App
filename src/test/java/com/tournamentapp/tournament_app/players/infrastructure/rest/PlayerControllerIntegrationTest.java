package com.tournamentapp.tournament_app.players.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tournamentapp.tournament_app.people.application.dto.RegisterPersonRequest;
import com.tournamentapp.tournament_app.people.application.usecase.RegisterPersonUseCase;
import com.tournamentapp.tournament_app.people.domain.model.Gender;
import com.tournamentapp.tournament_app.players.application.dto.RegisterPlayerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PlayerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterPersonUseCase registerPersonUseCase;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterPlayerSuccessfully() throws Exception {
        // Given - Primero crear una persona
        RegisterPersonRequest personRequest = new RegisterPersonRequest(
                "12345678",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 5, 15),
                Gender.MALE,
                null, null, null
        );
        var personResponse = registerPersonUseCase.execute(personRequest);

        RegisterPlayerRequest playerRequest = new RegisterPlayerRequest(
                personResponse.id(),
                "El Tigre",
                "http://photo.com/1.jpg"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/admin/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.personId").value(personResponse.id().toString()))
                .andExpect(jsonPath("$.nickname").value("El Tigre"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundWhenPersonDoesNotExist() throws Exception {
        // Given
        RegisterPlayerRequest request = new RegisterPlayerRequest(
                UUID.randomUUID(),
                "El Tigre",
                null
        );

        // When & Then
        mockMvc.perform(post("/api/v1/admin/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotAllowRegisterWithoutAuthentication() throws Exception {
        // Given
        RegisterPlayerRequest request = new RegisterPlayerRequest(
                UUID.randomUUID(),
                "El Tigre",
                null
        );

        // When & Then
        mockMvc.perform(post("/api/v1/admin/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowPublicAccessToGetPlayers() throws Exception {
        mockMvc.perform(get("/api/v1/public/players"))
                .andExpect(status().isOk());
    }
}
