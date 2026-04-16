package utp.edu.sistema_gestor_incidencias.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
}
