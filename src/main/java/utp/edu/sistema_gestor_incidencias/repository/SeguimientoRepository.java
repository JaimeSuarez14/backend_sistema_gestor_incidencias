package utp.edu.sistema_gestor_incidencias.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.model.Seguimiento;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
	Page<Seguimiento> findAllByOrderByFechaDesc(Pageable pageable) ;
	List<Seguimiento> findByIncidencia(Incidencia incidencia);
}
