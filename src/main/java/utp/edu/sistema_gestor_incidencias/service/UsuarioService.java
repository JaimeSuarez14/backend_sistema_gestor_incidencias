package utp.edu.sistema_gestor_incidencias.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
    public Usuario crearUsuario(Usuario usuario) {
       return this.usuarioRepository.save(usuario);
    }

    public Usuario modificarUsuario(Long id, Usuario datosNuevos) {
        Optional<Usuario> encontrado = this.usuarioRepository.findById(id);
        if (encontrado.isPresent()) {
            Usuario u = encontrado.get();
            u.setNombre(datosNuevos.getNombre());
            u.setCorreo(datosNuevos.getCorreo());
            u.setRol(datosNuevos.getRol());
            u.setArea(datosNuevos.getArea());
            u.setEstado(datosNuevos.getEstado());
            return this.usuarioRepository.save(u);
             
        }
        return null;
    }

    public List<Usuario> listarUsuarios() {
        return this.usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuario(Long id) {
    	Optional<Usuario> encontrado = this.usuarioRepository.findById(id);
        return encontrado;
    }
    
    public Page<Usuario> buscarTodoPorNombreDescendente(Pageable pageable){
    	return this.usuarioRepository.findAllByOrderByNombreDesc(pageable);
    }
}