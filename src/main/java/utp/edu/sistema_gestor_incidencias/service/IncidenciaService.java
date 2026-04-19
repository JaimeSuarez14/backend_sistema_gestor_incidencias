package utp.edu.sistema_gestor_incidencias.service;

import java.util.ArrayList;
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
	
	public Incidencia modificarIndicencia(Long id, Incidencia incidencia) {
		 return incidencias.stream()
			        .filter(i -> i.getId().equals(id))
			        .findFirst()
			        .map(i -> {
			            incidencia.setId(id);
			            incidencias.set(incidencias.indexOf(i), incidencia);
			            return incidencia;
			        })
			        .orElse(null);
	}
	
	public Optional<Incidencia> obtenerIncidencia(Long id) {
		return incidencias.stream()
				.filter(i -> i.getId().equals(id))
				.findFirst();
	}
}
