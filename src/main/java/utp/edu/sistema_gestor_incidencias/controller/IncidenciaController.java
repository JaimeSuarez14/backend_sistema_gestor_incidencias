package utp.edu.sistema_gestor_incidencias.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.service.IncidenciaService;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

@RestController
@RequestMapping("/api/incidencia")
public class IncidenciaController {
	
	@Autowired
	private IncidenciaService incidenciaService;
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity< ? > crearUsuario(@RequestBody Incidencia incidencia ){
		
		Long idUsu  = incidencia.getUsuario().getId();
		Long idTec  = incidencia.getTecnico().getId();
		
		var usuario =  usuarioService.obtenerUsuario(idUsu);
		var tecnico =  usuarioService.obtenerUsuario(idTec);
		
		if(!usuario.isPresent() | !tecnico.isPresent()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ese ID") ;
		
		incidencia.setUsuario( usuario.get() );
		incidencia.setTecnico( tecnico.get() );
		
		var newIncidencia = incidenciaService.crearIncidencia(incidencia);
		return ResponseEntity.ok().body(newIncidencia);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity< ? > modificarIndicencia( @PathVariable("id") Long id ,@RequestBody Incidencia incidencia ){
		
		Long idUsu  = incidencia.getUsuario().getId();
		Long idTec  = incidencia.getTecnico().getId();
		
		var usuario =  usuarioService.obtenerUsuario(idUsu);
		var tecnico =  usuarioService.obtenerUsuario(idTec);
		
		if(!usuario.isPresent() | !tecnico.isPresent()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ese ID") ;
		
		incidencia.setUsuario( usuario.get() );
		incidencia.setTecnico( tecnico.get() );
		
		var newIncidencia = incidenciaService.modificarIndicencia(id, incidencia);
		return ResponseEntity.ok().body(newIncidencia);
	}
}
