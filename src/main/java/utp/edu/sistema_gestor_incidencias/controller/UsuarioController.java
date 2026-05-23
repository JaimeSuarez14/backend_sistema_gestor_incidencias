package utp.edu.sistema_gestor_incidencias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import practica.spring_security.dto.roles.RoleDTO;
import utp.edu.sistema_gestor_incidencias.dto.ApiResponse;
import utp.edu.sistema_gestor_incidencias.dto.usuario.UsuarioResponseDto;
import utp.edu.sistema_gestor_incidencias.mappers.UsuarioMapper;
import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario modificado = usuarioService.modificarUsuario(id, usuario);
        if (modificado != null) return ResponseEntity.ok(modificado);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con id: " + id);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
    
    @GetMapping("/paginado")
    public PagedModel<Usuario> listarUsuariosPaginados(
    		@RequestParam(value="page", defaultValue = "0") int page,
    		@RequestParam(value = "size", defaultValue = "5") int size
    		) {
        Pageable pageable = PageRequest.of(page, size); 
        Page<Usuario> usuarios = this.usuarioService.buscarTodoPorNombreDescendente(pageable);
        return new PagedModel<>(usuarios);
    }
    
    @GetMapping("/tecnicos")
    public ResponseEntity<ApiResponse<List<Usuario>>> tecnicosDisponibles(){
    	List<Usuario> tecnicos = usuarioService.encontrarTecnicos("ROLE_TECNICO_NIVEL_1");

        ApiResponse<List<Usuario>> response = new ApiResponse<List<Usuario>>(
          true, "Busqueda eficiente", 200, tecnicos
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        var user = usuarioService.obtenerUsuario(id);
        if (user.isPresent()) return ResponseEntity.ok(user.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con id: " + id);
    }
    
    //actualizar Rol
    @PostMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> updateRole(@PathVariable Long id, @RequestBody RoleDTO role ){
    	Usuario user = usuarioService.actualizarRole(id, role.getRole());
        UsuarioResponseDto userResponseDto = usuarioMapper.toResponseDto(user);

        ApiResponse<UsuarioResponseDto> response = new ApiResponse<UsuarioResponseDto>(
          true, "Rol actualizado con exito!", 200, userResponseDto
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}