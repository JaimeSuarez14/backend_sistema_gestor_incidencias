package utp.edu.sistema_gestor_incidencias.model;

import java.util.Date;

public class Seguimiento {
	private Long idSeguimiento;
	private Incidencia incidencia;
	private String comentario;
	private Date fecha;
	private Estado estado;
	public Seguimiento(Long idSeguimiento, Incidencia incidencia, String comentario, Date fecha, Estado estado) {
		super();
		this.idSeguimiento = idSeguimiento;
		this.incidencia = incidencia;
		this.comentario = comentario;
		this.fecha = fecha;
		this.estado = estado;
	}
	
	
	public Seguimiento() {}


	public Long getIdSeguimiento() {
		return idSeguimiento;
	}


	public void setIdSeguimiento(Long idSeguimiento) {
		this.idSeguimiento = idSeguimiento;
	}


	public Incidencia getIncidencia() {
		return incidencia;
	}


	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}


	public String getComentario() {
		return comentario;
	}


	public void setComentario(String comentario) {
		this.comentario = comentario;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public Estado getEstado() {
		return estado;
	}


	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	
}
