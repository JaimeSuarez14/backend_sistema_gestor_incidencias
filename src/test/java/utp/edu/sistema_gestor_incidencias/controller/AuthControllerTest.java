package utp.edu.sistema_gestor_incidencias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.MediaType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import utp.edu.sistema_gestor_incidencias.dto.ApiResponse;
import utp.edu.sistema_gestor_incidencias.dto.usuario.UsuarioDTO;
import utp.edu.sistema_gestor_incidencias.dto.usuario.UsuarioResponseDto;
import utp.edu.sistema_gestor_incidencias.enums.Area;
import utp.edu.sistema_gestor_incidencias.mappers.UsuarioMapper;
import utp.edu.sistema_gestor_incidencias.model.Role;
import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.security.SpringSecurityConfig;
import utp.edu.sistema_gestor_incidencias.service.auth.AuthService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AuthController.class)
@Import(SpringSecurityConfig.class)
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean
  private AuthService authService;
  @MockitoBean
  private UsuarioMapper usuarioMapper;

  private UsuarioDTO userDtoEjemplo() {
    return new UsuarioDTO("jaime", "123456", "Jaime Suarez", "jaimito@gmail.com", Area.CONTABILIDAD);
  }

  // Jaime — POST /api/usuario
  @Test
  void crearUsuario_retorna201YUsuarioCreado() throws Exception {

    Usuario user = new Usuario();
    user.setId(1L);
    user.setUsername(this.userDtoEjemplo().getUsername());

    UsuarioResponseDto userResponseDto = new UsuarioResponseDto();
    userResponseDto.setUsername(user.getUsername());
    Set<Role> role = new HashSet<>();
    role.add(new Role(1L, "ROLE_EMPLEADO"));
    userResponseDto.setRoles(role);

    ApiResponse<UsuarioResponseDto> response = new ApiResponse<UsuarioResponseDto>(true, "Usuario Creado con exito!",
        201, userResponseDto);

    when(usuarioMapper.toEntity(any(UsuarioDTO.class))).thenReturn(user);
    when(authService.register(any(Usuario.class))).thenReturn(user);
    when(usuarioMapper.toResponseDto(any(Usuario.class))).thenReturn(userResponseDto);

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(response))
        .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Usuario Creado con exito!"))
        .andExpect(jsonPath("$.dato.username").value("jaime"))
        .andExpect(jsonPath("$.dato.roles[0].name").value("ROLE_EMPLEADO"));
  }
}
