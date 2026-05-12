package utp.edu.sistema_gestor_incidencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

}
