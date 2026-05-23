package utp.edu.sistema_gestor_incidencias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import utp.edu.sistema_gestor_incidencias.enums.Area;
import utp.edu.sistema_gestor_incidencias.enums.Estado;
import utp.edu.sistema_gestor_incidencias.mappers.UsuarioMapper;
import utp.edu.sistema_gestor_incidencias.model.*;
import utp.edu.sistema_gestor_incidencias.security.SpringSecurityConfig;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest( UsuarioController.class)
@Import(SpringSecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioMapper usuarioMapper;

    private Usuario usuarioEjemplo() {
    	Set<Role> role = new HashSet<>();
    	role.add(new Role(1L, "EMPLEADO"));
        return new Usuario(1L,"abel" , "123456","Abel Torres", "abel@utp.edu",Estado.ACTIVO,Area.SISTEMAS,
                role );
    }

    // Abel — POST /api/usuario
    /*@Test
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
    }*/

    // Jaime — PUT /api/usuario/{id}
    //@WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void modificarUsuario_retorna200YUsuarioModificado() throws Exception {
        Usuario usuario = usuarioEjemplo();
        usuario.setNombre("Abel Modificado");
        when(usuarioService.modificarUsuario(eq(1L), any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario))
                .with(user("adminUser").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Abel Modificado"));
    }

     // Jaime — PUT /api/usuario/{id}
    @Test
    void modificarUsuario_retorna404NotFound() throws Exception {
        Usuario usuario = usuarioEjemplo();
        usuario.setNombre("Abel Modificado");
        usuario.setId(99L);
        when(usuarioService.modificarUsuario(eq(99L), any(Usuario.class))).thenReturn(null);

        mockMvc.perform(put("/api/usuario/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario))
                .with(user("adminUser").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado con id: 99"));;
    }

    // Jaime — GET /api/usuario
    @Test
    void listarUsuarios_retorna200YListaDeUsuarios() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(List.of(usuarioEjemplo()));

        mockMvc.perform(get("/api/usuario")
            .with(user("adminUser").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Abel Torres"));
    }

    @Test
    void listarUsuariosPaginados_retorna200YListaDeUsuarios() throws Exception {
        Page<Usuario> pagina = new PageImpl<>(List.of(usuarioEjemplo()));
        when(usuarioService.buscarTodoPorNombreDescendente(any(Pageable.class))).thenReturn(pagina);

        mockMvc.perform(get("/api/usuario/paginado?page=0&size=2")
            .with(user("adminUser").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].nombre").value("Abel Torres"));
    }
    // Jaime — GET /api/usuario/{id}
    @Test
    void obtenerUsuario_retorna200CuandoExiste() throws Exception {
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuarioEjemplo()));

        mockMvc.perform(get("/api/usuario/1")
                .with(user("adminUser").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // Jaime — GET /api/usuario/{id}
    @Test
    void obtenerUsuario_retorna404CuandoNoExiste() throws Exception {
        when(usuarioService.obtenerUsuario(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuario/99")
                .with(user("adminUser").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // Jaime — GET /api/usuario/{id}
    @Test
    void obtenerUsuario_retorna403CuandoNoAutorizado() throws Exception {
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuarioEjemplo()));

        mockMvc.perform(get("/api/usuario/1")
                .with(user("usuarioComun").roles("EMPLEADO"))
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    
}