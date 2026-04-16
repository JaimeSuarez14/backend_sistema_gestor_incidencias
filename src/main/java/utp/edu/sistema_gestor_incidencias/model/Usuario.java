package utp.edu.sistema_gestor_incidencias.model;

public class Usuario {
	private Long id;
	private String nombre;
	private String correo;
	private Estado estado;
	private Rol rol;
	private Area area;
	
	public Usuario() {}

	public Usuario(Long id, String nombre, String correo, Rol rol, Area area, Estado estado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
		this.rol = rol;
		this.area = area;
		this.estado = estado;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	
	
}
