package com.tournamentapp.tournament_app.people.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tournamentapp.tournament_app.people.application.dto.RegisterPersonRequest;
import com.tournamentapp.tournament_app.people.domain.model.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterPersonSuccessfully() throws Exception {
        // Given
        RegisterPersonRequest request = new RegisterPersonRequest(
                "12345678",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 5, 15),
                Gender.MALE,
                "juan@example.com",
                "2234567890",
                null
        );

        // When & Then
        mockMvc.perform(post("/api/v1/admin/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.fullName").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void shouldNotAllowRegisterWithoutAuthentication() throws Exception {
        // Given
        RegisterPersonRequest request = new RegisterPersonRequest(
                "12345678",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 5, 15),
                Gender.MALE,
                null,
                null,
                null
        );

        // When & Then
        mockMvc.perform(post("/api/v1/admin/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequestForInvalidDni() throws Exception {
        // Given
        RegisterPersonRequest request = new RegisterPersonRequest(
                "123",  // DNI inválido
                "Juan",
                "Pérez",
                LocalDate.of(1990, 5, 15),
                Gender.MALE,
                null,
                null,
                null
        );

        // When & Then
        mockMvc.perform(post("/api/v1/admin/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.dni").exists());
    }

    @Test
    void shouldAllowPublicAccessToGetPerson() throws Exception {
        // Este test requiere que exista una persona en la BD
        // En un test real, primero crearías la persona o usarías datos de prueba

        mockMvc.perform(get("/api/v1/public/people/search")
                        .param("q", "Juan"))
                .andExpect(status().isOk());
    }
}
