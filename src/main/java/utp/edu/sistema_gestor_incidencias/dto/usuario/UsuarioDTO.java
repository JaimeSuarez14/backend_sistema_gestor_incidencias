package utp.edu.sistema_gestor_incidencias.dto.usuario;

import utp.edu.sistema_gestor_incidencias.enums.Area;

public class UsuarioDTO {
	
	private String username;
	private String password;
	private String nombre;
	private String correo;
	private Area area;
	
	public UsuarioDTO() {} 

	public UsuarioDTO(String username, String password, String nombre, String correo, Area area) {
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.correo = correo;
		this.area = area;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	
}
