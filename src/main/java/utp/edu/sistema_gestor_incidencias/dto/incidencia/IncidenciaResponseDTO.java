package utp.edu.sistema_gestor_incidencias.dto.incidencia;

public class IncidenciaResponseDTO {
	private Long id;
    private String titulo;
    private String estado;
    private String descripcion;
    private Long usuarioId;
    private Long tecnicoId;
    
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	public Long getTecnicoId() {
		return tecnicoId;
	}
	public void setTecnicoId(Long tecnicoId) {
		this.tecnicoId = tecnicoId;
	}
    
    
    
}
