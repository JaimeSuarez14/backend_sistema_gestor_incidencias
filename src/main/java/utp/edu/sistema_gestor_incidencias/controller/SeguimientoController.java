package utp.edu.sistema_gestor_incidencias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utp.edu.sistema_gestor_incidencias.model.Seguimiento;
import utp.edu.sistema_gestor_incidencias.service.SeguimientoService;

@RestController
@RequestMapping("/api/seguimiento")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    @PostMapping
    public ResponseEntity<Seguimiento> crearSeguimiento(@RequestBody Seguimiento seguimiento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seguimientoService.crearSeguimiento(seguimiento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarSeguimiento(@PathVariable Long id, @RequestBody Seguimiento seguimiento) {
        Seguimiento modificado = seguimientoService.modificarSeguimiento(id, seguimiento);
        if (modificado != null) return ResponseEntity.ok(modificado);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguimiento no encontrado con id: " + id);
    }

    @GetMapping
    public ResponseEntity<List<Seguimiento>> listarSeguimientos() {
        return ResponseEntity.ok(seguimientoService.listarSeguimientos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSeguimiento(@PathVariable Long id) {
        var seguimiento = seguimientoService.obtenerSeguimiento(id);
        if (seguimiento.isPresent()) return ResponseEntity.ok(seguimiento.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguimiento no encontrado con id: " + id);
    }
}