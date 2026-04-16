package utp.edu.sistema_gestor_incidencias.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.edu.sistema_gestor_incidencias.model.Usuario;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
	
	private List<Usuario> usuarios = new ArrayList<>();
	
	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario ){
		Usuario newUsuario = new Usuario();
		this.usuarios.add(newUsuario);
		return ResponseEntity.ok().body(newUsuario);
	}
	

}
