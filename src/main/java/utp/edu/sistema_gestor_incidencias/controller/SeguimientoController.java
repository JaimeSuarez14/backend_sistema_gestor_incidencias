package utp.edu.sistema_gestor_incidencias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.edu.sistema_gestor_incidencias.model.Seguimiento;
import utp.edu.sistema_gestor_incidencias.service.IncidenciaService;
import utp.edu.sistema_gestor_incidencias.service.SeguimientoService;

@RestController
@RequestMapping("/api/seguimiento")
public class SeguimientoController {
	@Autowired
	private SeguimientoService seguimientoService;
	
	@Autowired
	private IncidenciaService incidenciaService;
	
	@PostMapping
	public ResponseEntity<?> crearSeguimiento(@RequestBody Seguimiento seguimiento) {
		if (seguimiento.getIncidencia() == null || seguimiento.getIncidencia().getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe enviar el ID de la incidencia");
		}
		
		var incidencia = incidenciaService.obtenerIncidencia(seguimiento.getIncidencia().getId());
		
		if (incidencia.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Incidencia no encontrada con ese ID");
		}
		
		seguimiento.setIncidencia(incidencia.get());
		var newSeguimiento = seguimientoService.crearSeguimiento(seguimiento);
		return ResponseEntity.ok().body(newSeguimiento);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> modificarSeguimiento(@PathVariable("id") Long id, @RequestBody Seguimiento seguimiento) {
		if (seguimiento.getIncidencia() == null || seguimiento.getIncidencia().getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe enviar el ID de la incidencia");
		}
		
		var incidencia = incidenciaService.obtenerIncidencia(seguimiento.getIncidencia().getId());
		
		if (incidencia.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Incidencia no encontrada con ese ID");
		}
		
		seguimiento.setIncidencia(incidencia.get());
		var seguimientoActualizado = seguimientoService.modificarSeguimiento(id, seguimiento);
		
		if (seguimientoActualizado == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Seguimiento no encontrado con ese ID");
		}
		
		return ResponseEntity.ok().body(seguimientoActualizado);
	}

}
