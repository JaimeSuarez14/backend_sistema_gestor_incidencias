package utp.edu.sistema_gestor_incidencias.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Usuario;
@Service
public class UsuarioService {
	public List<Usuario> usuarios = new  ArrayList<>();
	private AtomicLong idGenerator = new AtomicLong(1);

	public Usuario  crearUsuario(Usuario usuario) {
		usuario.setId((Long)this.idGenerator.getAndIncrement());
		this.usuarios.add(usuario);
		return usuario;
	}
	
	public Optional<Usuario> obtenerUsuario( Long id) {
		Optional<Usuario> user = this.usuarios.stream()
			    .filter(u -> u.getId() == id)
			    .findFirst();
		return user;
	}
	
	public List<Usuario> listarUsuarios(){
		
		return this.usuarios;
		
	}
	public Usuario actualizarUsuario(Long id,Usuario usuarioActualizado) {
		
		for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                u.setNombre(usuarioActualizado.getNombre());
                u.setCorreo(usuarioActualizado.getCorreo());
                u.setArea(usuarioActualizado.getArea());
                u.setRol(usuarioActualizado.getRol());
                u.setEstado(usuarioActualizado.getEstado());

                return u;
            }
        }
		return null;
	}
	
	
}
