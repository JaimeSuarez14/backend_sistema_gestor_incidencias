package utp.edu.sistema_gestor_incidencias.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.edu.sistema_gestor_incidencias.model.Seguimiento;

@RestController
@RequestMapping("/api/seguimiento")
public class SeguimientoController {

    private List<Seguimiento> lista = new ArrayList<>();

    @GetMapping
    public List<Seguimiento> listar() {
        return lista;
    }

    @PostMapping
    public Seguimiento crear(@RequestBody Seguimiento seguimiento) {
        lista.add(seguimiento);
        return seguimiento;
    }

    @GetMapping("/{id}")
    public Seguimiento obtener(@PathVariable Long id) {
        return lista.stream()
                .filter(s -> s.getIdSeguimiento().equals(id))
                .findFirst()
                .orElse(null);
    }
}