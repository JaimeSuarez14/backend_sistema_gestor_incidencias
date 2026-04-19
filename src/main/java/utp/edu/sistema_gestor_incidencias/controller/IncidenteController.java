package utp.edu.sistema_gestor_incidencias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.service.IncidenteService;

@RestController
@RequestMapping("/api/incidencia")
public class IncidenteController {

    @Autowired
    private IncidenteService incidenteService;

    @PostMapping
    public ResponseEntity<Incidencia> crearIncidencia(@RequestBody Incidencia incidencia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidenteService.crearIncidencia(incidencia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarIncidencia(@PathVariable Long id, @RequestBody Incidencia incidencia) {
        Incidencia modificada = incidenteService.modificarIncidencia(id, incidencia);
        if (modificada != null) return ResponseEntity.ok(modificada);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrada con id: " + id);
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