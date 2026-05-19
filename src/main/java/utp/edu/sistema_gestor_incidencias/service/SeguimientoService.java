package utp.edu.sistema_gestor_incidencias.service;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Seguimiento;
import utp.edu.sistema_gestor_incidencias.repository.SeguimientoRepository;

@Service
public class SeguimientoService {
	@Autowired
	private SeguimientoRepository seguimientoRepository;


    public Seguimiento crearSeguimiento(Seguimiento seguimiento) {
        Date fechaActual = new Date();
        seguimiento.setFecha(fechaActual);
        return seguimientoRepository.save(seguimiento);
    }

    public Seguimiento modificarSeguimiento(Long id, Seguimiento datosNuevos) {
        Optional<Seguimiento> encontrado = seguimientoRepository.findById(id);;
        if (encontrado.isPresent()) {
            Seguimiento s = encontrado.get();
            s.setComentario(datosNuevos.getComentario());
            s.setEstado(datosNuevos.getEstado());
            return seguimientoRepository.save(s);
        }
        return null;
    }

    public List<Seguimiento> listarSeguimientos() {
        return seguimientoRepository.findAll();
    }

    public Optional<Seguimiento> obtenerSeguimiento(Long id) {
        return seguimientoRepository.findById(id);
    }
    
    public Page<Seguimiento> listarSeguimientosPaginado(Pageable pageable) {
        return seguimientoRepository.findAllByOrderByFechaDesc(pageable);
    }
}