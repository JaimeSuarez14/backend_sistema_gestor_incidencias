package utp.edu.sistema_gestor_incidencias.model;

public class Incidencia {
	private Long id;
	private String titulo;
	private String descripcion;
	private EstadoIncidencia estado;
	private Usuario usuario;
	private Usuario tecnico;
	
	public Incidencia() {}
	
	
	public Incidencia(Long id, String titulo, String descripcion, EstadoIncidencia estado, Usuario usuario, Usuario tecnico) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.estado = estado;
		this.usuario = usuario;
		this.tecnico = tecnico;
	}


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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstadoIncidencia getEstado() {
		return estado;
	}
	public void setEstado(EstadoIncidencia estado) {
		this.estado = estado;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getTecnico() {
		return tecnico;
	}

	public void setTecnico(Usuario tecnico) {
		this.tecnico = tecnico;
	}
	
	
	
	

}
