package utp.edu.sistema_gestor_incidencias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuario));
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        var user = usuarioService.obtenerUsuario(id);
        if (user.isPresent()) return ResponseEntity.ok(user.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con id: " + id);
    }
}