package utp.edu.sistema_gestor_incidencias.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.repository.IncidenciaRepository;


@Service
public class IncidenciaService {
	
	@Autowired
	private IncidenciaRepository incidenciaRepository;

    public Incidencia crearIncidencia(Incidencia incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    public Incidencia modificarIncidencia(Long id, Incidencia datosNuevos) {
        Optional<Incidencia> encontrada = incidenciaRepository.findById(id);;
        if (encontrada.isPresent()) {
            Incidencia i = encontrada.get();
            i.setTitulo(datosNuevos.getTitulo());
            i.setDescripcion(datosNuevos.getDescripcion());
            i.setEstado(datosNuevos.getEstado());
            i.setTecnico(datosNuevos.getTecnico());
            return incidenciaRepository.save(i);
        }
        return null;
    }

    public List<Incidencia> listarIncidencias() {
        return incidenciaRepository.findAll();
    }

    public Optional<Incidencia> obtenerIncidencia(Long id) {
        return incidenciaRepository.findById(id);
    }
    
    public Page<Incidencia> listarIncidenciasPaginado(Pageable pageable) {
        return incidenciaRepository.findAllByOrderByTituloDesc(pageable);
    }
    
    
}