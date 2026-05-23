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

import utp.edu.sistema_gestor_incidencias.dto.seguimiento.SeguimientoDTO;
import utp.edu.sistema_gestor_incidencias.exception.IncidenciaNotFoundException;
import utp.edu.sistema_gestor_incidencias.exception.UsuarioNoEncontradoException;
import utp.edu.sistema_gestor_incidencias.mappers.SeguimientoMapper;
import utp.edu.sistema_gestor_incidencias.model.Seguimiento;
import utp.edu.sistema_gestor_incidencias.service.SeguimientoService;

@RestController
@RequestMapping("/api/seguimiento")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;
    
    @Autowired
    private SeguimientoMapper seguimientoMapper;

    @PostMapping
    public ResponseEntity< ? > crearSeguimiento(@RequestBody SeguimientoDTO dto) {
    	try {
    		Seguimiento incidencia = seguimientoMapper.toEntity(dto);
    		var result = seguimientoService.crearSeguimiento(incidencia);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
    	} catch (UsuarioNoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  	
    	} catch (IncidenciaNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
    	}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( e.getMessage()); 
		}
    	
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarSeguimiento(@PathVariable Long id, @RequestBody Seguimiento seguimiento) {
		try {
			Seguimiento modificado = seguimientoService.modificarSeguimiento(id, seguimiento);
	        if (modificado != null) return ResponseEntity.ok(modificado);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguimiento no encontrado con id: " + id);
		} catch (IncidenciaNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
    	}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( e.getMessage()); 
		}
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
    
    @GetMapping("/{id}/seguimientos")
    public ResponseEntity<?> obtenerMisSeguimientos(@PathVariable Long id) {
    	try {
    		var seguimientos = seguimientoService.misSeguimientos(id);
            return ResponseEntity.ok(seguimientos);
    	} catch (IncidenciaNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
    	}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( e.getMessage()); 
		}
    }
    
}