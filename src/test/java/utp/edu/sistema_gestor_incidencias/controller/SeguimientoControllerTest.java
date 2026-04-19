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
import utp.edu.sistema_gestor_incidencias.service.SeguimientoService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeguimientoController.class)
class SeguimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SeguimientoService seguimientoService;

    @MockitoBean
    private IncidenciaService incidenciaService;

    private Seguimiento seguimientoEjemplo() {
        Usuario usuario = new Usuario(1L, "Stephani Lopez", "stephani@utp.edu",
                Rol.TECNICO_NIVEL_2, Area.SISTEMAS, Estado.ACTIVO);
        Incidencia incidencia = new Incidencia(1L, "PC no enciende", "Descripcion",
                EstadoIncidencia.ABIERTO, usuario, usuario);
        return new Seguimiento(1L, incidencia, "Revisando el equipo", new Date(), Estado.ACTIVO);
    }

    // Stephani — POST /api/seguimiento
    @Test
    void crearSeguimiento_retorna201YSeguimientoCreado() throws Exception {
        Seguimiento seguimiento = seguimientoEjemplo();
        when(incidenciaService.obtenerIncidencia(1L)).thenReturn(Optional.of(seguimiento.getIncidencia()));
        when(seguimientoService.crearSeguimiento(any(Seguimiento.class))).thenReturn(seguimiento);

        mockMvc.perform(post("/api/seguimiento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seguimiento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idSeguimiento").value(1L))
                .andExpect(jsonPath("$.comentario").value("Revisando el equipo"));
    }

    // Stephani — PUT /api/seguimiento/{id}
    @Test
    void modificarSeguimiento_retorna200YSeguimientoModificado() throws Exception {
        Seguimiento seguimiento = seguimientoEjemplo();
        seguimiento.setComentario("Equipo reparado");
        when(incidenciaService.obtenerIncidencia(1L)).thenReturn(Optional.of(seguimiento.getIncidencia()));
        when(seguimientoService.modificarSeguimiento(eq(1L), any(Seguimiento.class))).thenReturn(seguimiento);

        mockMvc.perform(put("/api/seguimiento/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seguimiento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Equipo reparado"));
    }

    // Jamil — GET /api/seguimiento
    @Test
    void listarSeguimientos_retorna200YListaDeSeguimientos() throws Exception {
        when(seguimientoService.listarSeguimientos()).thenReturn(List.of(seguimientoEjemplo()));

        mockMvc.perform(get("/api/seguimiento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].comentario").value("Revisando el equipo"));
    }

    // Jamil — GET /api/seguimiento/{id}
    @Test
    void obtenerSeguimiento_retorna200CuandoExiste() throws Exception {
        when(seguimientoService.obtenerSeguimiento(1L)).thenReturn(Optional.of(seguimientoEjemplo()));

        mockMvc.perform(get("/api/seguimiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSeguimiento").value(1L));
    }

    @Test
    void obtenerSeguimiento_retorna404CuandoNoExiste() throws Exception {
        when(seguimientoService.obtenerSeguimiento(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/seguimiento/99"))
                .andExpect(status().isNotFound());
     }
    
    


    // Seguimiento // 
    @Test
    void listarSeguimientos_retornaListaVacia() throws Exception {
        when(seguimientoService.listarSeguimientos()).thenReturn(List.of());

        mockMvc.perform(get("/api/seguimiento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void listarSeguimientos_retornaMultiplesSeguimientos() throws Exception {
        Seguimiento s1 = seguimientoEjemplo();

        Usuario usuario = new Usuario(2L, "Jamil Alarcon", "jml@utp.edu",
                Rol.TECNICO_NIVEL_2, Area.SISTEMAS, Estado.ACTIVO);
        Incidencia incidencia = new Incidencia(2L, "Monitor falla", "Descripcion 2",
                EstadoIncidencia.ABIERTO, usuario, usuario);
        Seguimiento s2 = new Seguimiento(2L, incidencia, "Cambio de monitor", new Date(), Estado.ACTIVO);

        when(seguimientoService.listarSeguimientos()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/seguimiento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void obtenerSeguimiento_validaComentarioCuandoExiste() throws Exception {
        when(seguimientoService.obtenerSeguimiento(1L)).thenReturn(Optional.of(seguimientoEjemplo()));

        mockMvc.perform(get("/api/seguimiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSeguimiento").value(1L))
                .andExpect(jsonPath("$.comentario").value("Revisando el equipo"));
    }

    @Test
    void obtenerSeguimiento_idInvalido_retorna404() throws Exception {
        when(seguimientoService.obtenerSeguimiento(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/seguimiento/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearSeguimiento_cuandoIncidenciaNoExiste_retorna404() throws Exception {
        var seguimiento = seguimientoEjemplo();
        var incidenciaInexistente = new Incidencia();
        incidenciaInexistente.setId(99L); // ID que no existe
        seguimiento.setIncidencia(incidenciaInexistente);

        // 2. Simular el comportamiento del servicio: el Optional debe estar vacío
        when(incidenciaService.obtenerIncidencia(99L)).thenReturn(Optional.empty());

        // 3. Ejecutar la petición y verificar
        mockMvc.perform(post("/api/seguimiento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seguimiento)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Incidencia no encontrado con ese ID"));
        
        // Verificación opcional: asegurar que el servicio de seguimiento NUNCA se llamó
        verify(seguimientoService, never()).crearSeguimiento(any(Seguimiento.class));
    }

     @Test
    void modificarSeguimiento_cuandoIncidenciaNoExiste_retorna404() throws Exception {
        var seguimiento = seguimientoEjemplo();
        seguimiento.setComentario("Equipo reparado, puede recogerlo.");
        seguimiento.getIncidencia().setId(99L); // ID que no existe
        when(incidenciaService.obtenerIncidencia(99L)).thenReturn(Optional.empty());
        when(seguimientoService.modificarSeguimiento(eq(1L), any(Seguimiento.class))).thenReturn(seguimiento);

        mockMvc.perform(put("/api/seguimiento/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seguimiento)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Incidencia no encontrado con ese ID"));
    }
}
