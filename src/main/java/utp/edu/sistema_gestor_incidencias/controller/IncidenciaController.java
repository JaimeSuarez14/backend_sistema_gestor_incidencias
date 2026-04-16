package utp.edu.sistema_gestor_incidencias.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.model.Usuario;

@RestController
@RequestMapping("/api/incidencia")
public class IncidenciaController {
	private List<Incidencia> incidencias = new ArrayList<>();
	private AtomicLong idGenerator = new AtomicLong(1);
	
	@PostMapping
	public ResponseEntity<Incidencia> crearUsuario(@RequestBody Incidencia incidencia ){
		incidencia.setId((Long)this.idGenerator.getAndIncrement());
		
		this.incidencias.add(incidencia);
		return ResponseEntity.ok().body(incidencia);
	}
}
