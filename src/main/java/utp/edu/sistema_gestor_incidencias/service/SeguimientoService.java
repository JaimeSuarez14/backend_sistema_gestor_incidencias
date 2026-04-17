package utp.edu.sistema_gestor_incidencias.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Seguimiento;

@Service
public class SeguimientoService {
	private final List<Seguimiento> seguimientos = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1);
	
	public Seguimiento crearSeguimiento(Seguimiento seguimiento) {
		seguimiento.setIdSeguimiento(idGenerator.getAndIncrement());
		seguimientos.add(seguimiento);
		return seguimiento;
	}
	
	public Seguimiento modificarSeguimiento(Long id, Seguimiento seguimiento) {
		return seguimientos.stream()
				.filter(s -> s.getIdSeguimiento().equals(id))
				.findFirst()
				.map(s -> {
					seguimiento.setIdSeguimiento(id);
					seguimientos.set(seguimientos.indexOf(s), seguimiento);
					return seguimiento;
				})
				.orElse(null);
	}
}
