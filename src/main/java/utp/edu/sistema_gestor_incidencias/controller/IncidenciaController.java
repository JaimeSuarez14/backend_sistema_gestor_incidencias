package utp.edu.sistema_gestor_incidencias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.service.IncidenciaService;
import utp.edu.sistema_gestor_incidencias.service.UsuarioService;

@RestController
@RequestMapping("/api/incidencia")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenteService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    
    @PostMapping
    public ResponseEntity< ? > crearIncidencia(@RequestBody Incidencia incidencia ){
		
		Long idUsu  = incidencia.getUsuario().getId();
		Long idTec  = incidencia.getTecnico().getId();
		
		var usuario =  usuarioService.obtenerUsuario(idUsu);
		var tecnico =  usuarioService.obtenerUsuario(idTec);
		
		if(!usuario.isPresent() | !tecnico.isPresent()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ese ID") ;
		
		incidencia.setUsuario( usuario.get() );
		incidencia.setTecnico( tecnico.get() );
		
		var newIncidencia = this.incidenteService.crearIncidencia(incidencia);
		return ResponseEntity.status(HttpStatus.CREATED).body(newIncidencia);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity< ? > modificarIndicencia( @PathVariable("id") Long id ,@RequestBody Incidencia incidencia ){
		
		Long idUsu  = incidencia.getUsuario().getId();
		Long idTec  = incidencia.getTecnico().getId();
		
		var usuario =  usuarioService.obtenerUsuario(idUsu);
		var tecnico =  usuarioService.obtenerUsuario(idTec);
		
		if(!usuario.isPresent() | !tecnico.isPresent()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ese ID") ;
		
		incidencia.setUsuario( usuario.get() );
		incidencia.setTecnico( tecnico.get() );
		
		var newIncidencia = this.incidenteService.modificarIncidencia(id, incidencia);
		return ResponseEntity.ok().body(newIncidencia);
	}
	
    @GetMapping
    public ResponseEntity<List<Incidencia>> listarIncidencias() {
        return ResponseEntity.ok(incidenteService.listarIncidencias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerIncidencia(@PathVariable Long id) {
        var incidencia = incidenteService.obtenerIncidencia(id);
        if (incidencia.isPresent()) return ResponseEntity.ok(incidencia.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrada con id: " + id);
    }
}