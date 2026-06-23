package utp.edu.sistema_gestor_incidencias.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.model.Usuario;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
	Page<Incidencia> findAllByOrderByTituloDesc(Pageable pageable) ;
	List<Incidencia> findByUsuario(Usuario usuario);
	List<Incidencia> findByTecnico(Usuario tecnico);
}
