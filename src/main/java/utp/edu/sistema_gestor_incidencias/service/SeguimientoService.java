package utp.edu.sistema_gestor_incidencias.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Seguimiento;

@Service
public class SeguimientoService {

    private List<Seguimiento> seguimientos = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public Seguimiento crearSeguimiento(Seguimiento seguimiento) {
        seguimiento.setIdSeguimiento(idGenerator.getAndIncrement());
        this.seguimientos.add(seguimiento);
        return seguimiento;
    }

    public Seguimiento modificarSeguimiento(Long id, Seguimiento datosNuevos) {
        Optional<Seguimiento> encontrado = obtenerSeguimiento(id);
        if (encontrado.isPresent()) {
            Seguimiento s = encontrado.get();
            s.setComentario(datosNuevos.getComentario());
            s.setEstado(datosNuevos.getEstado());
            return s;
        }
        return null;
    }

    public List<Seguimiento> listarSeguimientos() {
        return Collections.unmodifiableList(seguimientos);
    }

    public Optional<Seguimiento> obtenerSeguimiento(Long id) {
        return this.seguimientos.stream()
                .filter(s -> s.getIdSeguimiento().equals(id))
                .findFirst();
    }
}