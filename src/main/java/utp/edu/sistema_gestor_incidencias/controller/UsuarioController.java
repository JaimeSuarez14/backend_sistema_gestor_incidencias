package utp.edu.sistema_gestor_incidencias.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	  // CREAR USUARIO
	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario ){
		var newUsuario =  usuarioService.crearUsuario(usuario);
		return ResponseEntity.ok().body(newUsuario);
	}
	 // OBTENER USUARIO
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerUsuario(@PathVariable("id") Long id){
		var user = usuarioService.obtenerUsuario(id);
		if(user.isPresent()) {
			return ResponseEntity.ok().body(user.get());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con el "+ id);
		}
			    
	}
	
    // LISTAR USUARIO
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
    	var usuarios=usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }
    
    // USUARIO MODIFICAR
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
    	var usuario=usuarioService.actualizarUsuario(id, usuarioActualizado);
        if (usuario != null) 
        	return ResponseEntity.ok().body(usuario);
        return ResponseEntity.notFound().build();
    } 
}
