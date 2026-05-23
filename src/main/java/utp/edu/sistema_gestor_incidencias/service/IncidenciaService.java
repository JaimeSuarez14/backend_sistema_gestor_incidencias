package utp.edu.sistema_gestor_incidencias.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.dto.incidencia.IncidenciaDTO;
import utp.edu.sistema_gestor_incidencias.enums.Estado;
import utp.edu.sistema_gestor_incidencias.enums.EstadoIncidencia;
import utp.edu.sistema_gestor_incidencias.exception.UsuarioNoEncontradoException;
import utp.edu.sistema_gestor_incidencias.model.Incidencia;
import utp.edu.sistema_gestor_incidencias.model.Seguimiento;
import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.repository.IncidenciaRepository;


@Service
public class IncidenciaService {
	
	@Autowired
	private IncidenciaRepository incidenciaRepository;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private SeguimientoService seguimientoService;


    public Incidencia crearIncidencia(IncidenciaDTO dto) {
    	Incidencia incidencia = new Incidencia();
    	
		var usuario =  usuarioService.obtenerUsuarioUsername()
				.orElseThrow(()-> new UsuarioNoEncontradoException("El usuario no encontrado"));
		
		List<Usuario > tecnicosLibres= usuarioService.encontrarTecnicos("ROLE_TECNICO_NIVEL_1");
		Usuario tecnico =  null;
		if (!tecnicosLibres.isEmpty()) {
            // Asigna el primer técnico que no tiene tareas pendientes
			tecnico = tecnicosLibres.get(0);
			
			if(usuario.getId() == tecnico.getId()) {
				throw new RuntimeException( "No pueden ser iguales el usuario con tecnico" );
			}
			incidencia.setTecnico(tecnico);
        } else {
            // Si todos están ocupados, puedes dejarlo en null para asignación manual posterior
        	incidencia.setTecnico(null);
        }
	
		incidencia.setTitulo(dto.getTitulo());
		incidencia.setDescripcion(dto.getDescripcion());
		incidencia.setUsuario( usuario );
    	incidencia.setEstado(EstadoIncidencia.PENDIENTE);
    	
    	Incidencia newIncidencia = incidenciaRepository.save(incidencia);
    	
    	Seguimiento seguimiento = new Seguimiento();
    	seguimiento.setIncidencia(newIncidencia);
    	seguimiento.setEstado(Estado.ACTIVO);
    	seguimiento.setComentario("Incidencia creada por "+usuario.getUsername());
    	
    	seguimientoService.crearSeguimiento(seguimiento);
        
    	return newIncidencia;
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
    
    public List<Incidencia> misIncidencias(){
    	var usuario =  usuarioService.obtenerUsuarioUsername().
    		    orElseThrow(()-> new UsuarioNoEncontradoException("El usuario no encontrado"));
    	return incidenciaRepository.findByUsuario(usuario);
    }
    
    
}