package utp.edu.sistema_gestor_incidencias.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import utp.edu.sistema_gestor_incidencias.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);
	boolean existsByCorreo(String correo);
	boolean existsByUsername(String username);
	
	Page<Usuario> findAllByOrderByNombreDesc(Pageable pageable) ;
	
	@Query("select u from Usuario u left join fetch u.roles where u.id = ?1")
	Optional<Usuario> getUserWithRoles(Long id);
	
	@Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.name = :nombreRol AND NOT EXISTS " +
	           "(SELECT i FROM Incidencia i WHERE i.tecnico = u AND i.estado = 'PENDIENTE')")
	List<Usuario> findTecnicosDisponibles(@Param("nombreRol") String nombreRol);
}
