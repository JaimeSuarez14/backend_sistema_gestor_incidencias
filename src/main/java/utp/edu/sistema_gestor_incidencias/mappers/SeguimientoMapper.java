package utp.edu.sistema_gestor_incidencias.mappers;

import org.springframework.stereotype.Component;

import utp.edu.sistema_gestor_incidencias.dto.seguimiento.SeguimientoDTO;
import utp.edu.sistema_gestor_incidencias.model.Seguimiento;

@Component
public class SeguimientoMapper {
	public Seguimiento toEntity(SeguimientoDTO dto) {
		if(dto == null) return null;
		Seguimiento seguimiento = new Seguimiento();
		seguimiento.setComentario(dto.getComentario());
		seguimiento.setEstado(dto.getEstado());
		seguimiento.getIncidencia().setId(dto.getIdIncidencia());
		
		return seguimiento;
	}
}
