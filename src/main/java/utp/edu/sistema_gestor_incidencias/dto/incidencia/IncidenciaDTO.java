package utp.edu.sistema_gestor_incidencias.dto.incidencia;

public class IncidenciaDTO {

    private String titulo;
    private String descripcion;
    
    
	public IncidenciaDTO(String titulo, String descripcion) {
		this.titulo = titulo;
		this.descripcion = descripcion;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
    
    
}
