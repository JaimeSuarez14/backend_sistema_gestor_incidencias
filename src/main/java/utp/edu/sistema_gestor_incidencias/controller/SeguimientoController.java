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

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
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
    public ResponseEntity< ? > crearSeguimiento(@RequestBody Seguimiento seguimiento) {
        var  incidencia = incidenciaService.obtenerIncidencia(seguimiento.getIncidencia().getId());
        
		if(!incidencia.isPresent() ) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrado con ese ID") ;
        seguimiento.setIncidencia(incidencia.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(seguimientoService.crearSeguimiento(seguimiento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarSeguimiento(@PathVariable Long id, @RequestBody Seguimiento seguimiento) {

        var  incidencia = incidenciaService.obtenerIncidencia(seguimiento.getIncidencia().getId());
		if(!incidencia.isPresent() ) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrado con ese ID") ;
        seguimiento.setIncidencia(incidencia.get());

        Seguimiento modificado = seguimientoService.modificarSeguimiento(id, seguimiento);
        if (modificado != null) return ResponseEntity.ok(modificado);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguimiento no encontrado con id: " + id);
    }

    @GetMapping
    public ResponseEntity<List<Seguimiento>> listarSeguimientos() {
        return ResponseEntity.ok(seguimientoService.listarSeguimientos());
    }
    
    @GetMapping("/paginado")
    public PagedModel<Seguimiento> seguimientoPaginado(
    		@RequestParam(value="page", defaultValue = "0") int page,
    		@RequestParam(value = "size", defaultValue = "5") int size
    		) {
        Pageable pageable = PageRequest.of(page, size); 
        Page<Seguimiento> seguimientos = this.seguimientoService.listarSeguimientosPaginado(pageable);
        return new PagedModel<>(seguimientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSeguimiento(@PathVariable Long id) {
        var seguimiento = seguimientoService.obtenerSeguimiento(id);
        if (seguimiento.isPresent()) return ResponseEntity.ok(seguimiento.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguimiento no encontrado con id: " + id);
    }
    
    
}