package utp.edu.sistema_gestor_incidencias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import utp.edu.sistema_gestor_incidencias.model.*;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo() {
        return new Usuario(1L, "Abel Torres", "abel@utp.edu",
                Rol.EMPLEADO, Area.SISTEMAS, Estado.ACTIVO);
    }

    // Abel — POST /api/usuario
    @Test
    void crearUsuario_retorna201YUsuarioCreado() throws Exception {
        Usuario usuario = usuarioEjemplo();
        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Abel Torres"))
                .andExpect(jsonPath("$.correo").value("abel@utp.edu"));
    }

    // Abel — PUT /api/usuario/{id}
    @Test
    void modificarUsuario_retorna200YUsuarioModificado() throws Exception {
        Usuario usuario = usuarioEjemplo();
        usuario.setNombre("Abel Modificado");
        when(usuarioService.modificarUsuario(eq(1L), any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Abel Modificado"));
    }

    // Abel — GET /api/usuario
    @Test
    void listarUsuarios_retorna200YListaDeUsuarios() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(List.of(usuarioEjemplo()));

        mockMvc.perform(get("/api/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Abel Torres"));
    }

    // Jaime — GET /api/usuario/{id}
    @Test
    void obtenerUsuario_retorna200CuandoExiste() throws Exception {
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuarioEjemplo()));

        mockMvc.perform(get("/api/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void obtenerUsuario_retorna404CuandoNoExiste() throws Exception {
        when(usuarioService.obtenerUsuario(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuario/99"))
                .andExpect(status().isNotFound());
    }
}