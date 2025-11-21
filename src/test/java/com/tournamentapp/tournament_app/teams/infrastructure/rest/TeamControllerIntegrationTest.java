package com.tournamentapp.tournament_app.teams.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tournamentapp.tournament_app.teams.application.dto.CreateTeamRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeamControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateTeamSuccessfully() throws Exception {
        // Given
        CreateTeamRequest request = new CreateTeamRequest(
                "Boca Juniors",
                "http://example.com/boca.png"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/admin/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Boca Juniors"))
                .andExpect(jsonPath("$.logoUrl").value("http://example.com/boca.png"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnConflictWhenTeamNameAlreadyExists() throws Exception {
        // Given - Crear primer equipo
        CreateTeamRequest request1 = new CreateTeamRequest("River Plate", null);
        mockMvc.perform(post("/api/v1/admin/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        // Intentar crear equipo con mismo nombre
        CreateTeamRequest request2 = new CreateTeamRequest("River Plate", null);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Team with name 'River Plate' already exists"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        // Given
        CreateTeamRequest request = new CreateTeamRequest("", null);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").exists());
    }

    @Test
    void shouldNotAllowCreateWithoutAuthentication() throws Exception {
        // Given
        CreateTeamRequest request = new CreateTeamRequest("Boca Juniors", null);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowPublicAccessToGetTeams() throws Exception {
        mockMvc.perform(get("/api/v1/public/teams"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSearchTeamsByName() throws Exception {
        mockMvc.perform(get("/api/v1/public/teams/search")
                        .param("q", "Boca"))
                .andExpect(status().isOk());
    }
}
