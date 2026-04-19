package utp.edu.sistema_gestor_incidencias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import utp.edu.sistema_gestor_incidencias.model.*;
import utp.edu.sistema_gestor_incidencias.service.IncidenteService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidenteController.class)
class IncidenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private IncidenteService incidenteService;

    private Incidencia incidenciaEjemplo() {
        Usuario usuario = new Usuario(1L, "Jaime Ruiz", "jaime@utp.edu",
                Rol.EMPLEADO, Area.CONTABILIDAD, Estado.ACTIVO);
        Usuario tecnico = new Usuario(2L, "Johan Gonzales", "johan@utp.edu",
                Rol.TECNICO_NIVEL_1, Area.SISTEMAS, Estado.ACTIVO);
        return new Incidencia(1L, "PC no enciende", "El equipo no responde al inicio",
                EstadoIncidencia.ABIERTO, usuario, tecnico);
    }

    // Jaime — POST /api/incidencia
    @Test
    void crearIncidencia_retorna201YIncidenciaCreada() throws Exception {
        Incidencia incidencia = incidenciaEjemplo();
        when(incidenteService.crearIncidencia(any(Incidencia.class))).thenReturn(incidencia);

        mockMvc.perform(post("/api/incidencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidencia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("PC no enciende"))
                .andExpect(jsonPath("$.estado").value("ABIERTO"));
    }

    // Jaime — PUT /api/incidencia/{id}
    @Test
    void modificarIncidencia_retorna200YIncidenciaModificada() throws Exception {
        Incidencia incidencia = incidenciaEjemplo();
        incidencia.setEstado(EstadoIncidencia.PENDIENTE);
        when(incidenteService.modificarIncidencia(eq(1L), any(Incidencia.class))).thenReturn(incidencia);

        mockMvc.perform(put("/api/incidencia/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidencia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    // Johan — GET /api/incidencia
    @Test
    void listarIncidencias_retorna200YListaDeIncidencias() throws Exception {
        when(incidenteService.listarIncidencias()).thenReturn(List.of(incidenciaEjemplo()));

        mockMvc.perform(get("/api/incidencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("PC no enciende"));
    }

    // Johan — GET /api/incidencia/{id}
    @Test
    void obtenerIncidencia_retorna200CuandoExiste() throws Exception {
        when(incidenteService.obtenerIncidencia(1L)).thenReturn(Optional.of(incidenciaEjemplo()));

        mockMvc.perform(get("/api/incidencia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void obtenerIncidencia_retorna404CuandoNoExiste() throws Exception {
        when(incidenteService.obtenerIncidencia(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/incidencia/99"))
                .andExpect(status().isNotFound());
    }
}