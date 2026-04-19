package utp.edu.sistema_gestor_incidencias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import utp.edu.sistema_gestor_incidencias.model.*;
import utp.edu.sistema_gestor_incidencias.service.IncidenciaService;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidenciaController.class)
class IncidenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private IncidenciaService incidenteService;
    
    @MockitoBean
    private UsuarioService usuarioService; 

    private Usuario usuarioEjemplo() {
        return new Usuario(1L, "Jaime Ruiz", "jaime@utp.edu",
                Rol.EMPLEADO, Area.CONTABILIDAD, Estado.ACTIVO);
    }
    
    private Usuario tecnicoEjemplo() {
        return new Usuario(2L, "Johan Gonzales", "johan@utp.edu",
                Rol.TECNICO_NIVEL_1, Area.SISTEMAS, Estado.ACTIVO);
    }

    private Incidencia incidenciaEjemplo() {
        return new Incidencia(1L, "PC no enciende", "El equipo no responde al inicio",
                EstadoIncidencia.ABIERTO, usuarioEjemplo(), tecnicoEjemplo());
    }

    // Jaime — POST /api/incidencia
    @Test
    void crearIncidencia_retorna201YIncidenciaCreada() throws Exception {
        Incidencia incidencia = incidenciaEjemplo();
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuarioEjemplo()));
        when(usuarioService.obtenerUsuario(2L)).thenReturn(Optional.of(tecnicoEjemplo()));
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
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuarioEjemplo()));
        when(usuarioService.obtenerUsuario(2L)).thenReturn(Optional.of(tecnicoEjemplo()));
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

    // Jaime — POST /api/incidencia
    @Test
    void crearIncidencia_retorna404NotFound() throws Exception {
        Incidencia incidencia = incidenciaEjemplo();
        incidencia.getUsuario().setId(99L);;
        when(usuarioService.obtenerUsuario(99L)).thenReturn(Optional.empty());
        when(usuarioService.obtenerUsuario(2L)).thenReturn(Optional.of(tecnicoEjemplo()));
        when(incidenteService.crearIncidencia(any(Incidencia.class))).thenReturn(incidencia);

        mockMvc.perform(post("/api/incidencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidencia)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado con ese ID"));
               
    }

    // Jaime — put /api/incidencia/{id}
    @Test
    void modificarIncidencia_retorna404NotFound() throws Exception {
        Incidencia incidencia = incidenciaEjemplo();
        incidencia.setDescripcion("El equipo prende, pero demora como 5 minutos en iniciar sesión");
        incidencia.getUsuario().setId(99L);;
        when(usuarioService.obtenerUsuario(99L)).thenReturn(Optional.empty());
        when(usuarioService.obtenerUsuario(2L)).thenReturn(Optional.of(tecnicoEjemplo()));
        when(incidenteService.crearIncidencia(any(Incidencia.class))).thenReturn(incidencia);

        mockMvc.perform(put("/api/incidencia/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidencia)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado con ese ID"));
               
    }
}