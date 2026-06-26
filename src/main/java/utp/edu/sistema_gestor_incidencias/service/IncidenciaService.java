package utp.edu.sistema_gestor_incidencias.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.dto.incidencia.EstadoIncidenciaRequest;
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

	private IncidenciaRepository incidenciaRepository;

	private UsuarioService usuarioService;

	private SeguimientoService seguimientoService;

	public IncidenciaService(IncidenciaRepository incidenciaRepository, UsuarioService usuarioService,
			SeguimientoService seguimientoService) {
		this.incidenciaRepository = incidenciaRepository;
		this.usuarioService = usuarioService;
		this.seguimientoService = seguimientoService;
	}

	public Incidencia crearIncidencia(IncidenciaDTO dto) {
		Incidencia incidencia = new Incidencia();

		var usuario = usuarioService.obtenerUsuarioSession()
				.orElseThrow(() -> new UsuarioNoEncontradoException("El usuario no encontrado"));

		List<Usuario> tecnicosLibres = usuarioService.encontrarTecnicos("ROLE_TECNICO_NIVEL_1");
		Usuario tecnico = null;
		if (!tecnicosLibres.isEmpty()) {
			// Asigna el primer técnico que no tiene tareas pendientes
			tecnico = tecnicosLibres.get(0);

			if (usuario.getId() == tecnico.getId()) {
				throw new RuntimeException("No pueden ser iguales el usuario con tecnico");
			}
			incidencia.setTecnico(tecnico);
		} else {
			// Si todos están ocupados, puedes dejarlo en null para asignación manual
			// posterior
			incidencia.setTecnico(null);
		}
		Date fechaActual = new Date();
		incidencia.setFechaCreacion(fechaActual);
		incidencia.setTitulo(dto.getTitulo());
		incidencia.setDescripcion(dto.getDescripcion());
		incidencia.setUsuario(usuario);
		incidencia.setEstado(EstadoIncidencia.PENDIENTE);

		Incidencia newIncidencia = incidenciaRepository.save(incidencia);

		Seguimiento seguimiento = new Seguimiento();
		seguimiento.setIncidencia(newIncidencia);
		seguimiento.setEstado(Estado.ACTIVO);
		seguimiento.setComentario("Incidencia creada por " + usuario.getUsername());

		seguimientoService.crearSeguimiento(seguimiento);

		return newIncidencia;
	}

	public Incidencia modificarIncidencia(Long id, Incidencia datosNuevos) {
		Optional<Incidencia> encontrada = incidenciaRepository.findById(id);
		;
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
		return incidenciaRepository.findAllByOrderByFechaCreacionDesc(pageable);
	}
	
	public Page<Incidencia> listarIncidenciasPaginadoUsuarioTecnicos(Pageable pageable) {
		return incidenciaRepository.findAllByOrderByFechaCreacionDesc(pageable);
	}

	public List<Incidencia> misIncidencias() {
		var usuario = usuarioService.obtenerUsuarioSession()
				.orElseThrow(() -> new UsuarioNoEncontradoException("El usuario no encontrado"));
		List<Incidencia> comoUsuario = incidenciaRepository.findByUsuario(usuario);
		List<Incidencia> comoTecnico = incidenciaRepository.findByTecnico(usuario);
		return Stream.concat(comoUsuario.stream(), comoTecnico.stream())
				.distinct()
				.collect(Collectors.toList());
	}

	public Page<Incidencia> misIncidenciasPage(Pageable pageable){
		var usuario = usuarioService.obtenerUsuarioSession()
				.orElseThrow(() -> new UsuarioNoEncontradoException("El usuario no encontrado"));
		return incidenciaRepository.findByUsuarioOrTecnico(usuario, pageable);
	}

	public Incidencia modificarEstado(EstadoIncidenciaRequest est){
		Incidencia incidencia = incidenciaRepository.findById(est.getIdIncidencia()).orElseThrow(() -> new UsuarioNoEncontradoException("El usuario no encontrado"));
		incidencia.setEstado(est.getEstado());
		return incidenciaRepository.save(incidencia);
	}

}