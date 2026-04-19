package utp.edu.sistema_gestor_incidencias.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;


@Service
public class IncidenciaService {

    private List<Incidencia> incidencias = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public Incidencia crearIncidencia(Incidencia incidencia) {
        incidencia.setId(idGenerator.getAndIncrement());
        this.incidencias.add(incidencia);
        return incidencia;
    }

    public Incidencia modificarIncidencia(Long id, Incidencia datosNuevos) {
        Optional<Incidencia> encontrada = obtenerIncidencia(id);
        if (encontrada.isPresent()) {
            Incidencia i = encontrada.get();
            i.setTitulo(datosNuevos.getTitulo());
            i.setDescripcion(datosNuevos.getDescripcion());
            i.setEstado(datosNuevos.getEstado());
            i.setTecnico(datosNuevos.getTecnico());
            return i;
        }
        return null;
    }

    public List<Incidencia> listarIncidencias() {
        return Collections.unmodifiableList(incidencias);
    }

    public Optional<Incidencia> obtenerIncidencia(Long id) {
        return this.incidencias.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }
}