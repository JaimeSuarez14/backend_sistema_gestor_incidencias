package utp.edu.sistema_gestor_incidencias.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import utp.edu.sistema_gestor_incidencias.model.Role;
import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.repository.RoleRepository;
import utp.edu.sistema_gestor_incidencias.repository.UsuarioRepository;
import utp.edu.sistema_gestor_incidencias.security.utils.SecurityUtils;

@Service
public class UsuarioService {


	private UsuarioRepository usuarioRepository;
	private RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
		this.usuarioRepository = usuarioRepository;
		this.roleRepository = roleRepository;
	}

	public Usuario modificarUsuario(Long id, Usuario datosNuevos) {
		Optional<Usuario> encontrado = usuarioRepository.getUserWithRoles(id);
        if (encontrado.isPresent()) {
            Usuario u = encontrado.get();
            u.setUsername(datosNuevos.getUsername());
            u.setNombre(datosNuevos.getNombre());
            u.setCorreo(datosNuevos.getCorreo());
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
    
    public Optional<Usuario> obtenerUsuarioUsername() {
    	String username = SecurityUtils.getCurrentUsername();
    	Optional<Usuario> encontrado = usuarioRepository.findByUsername(username);
        return encontrado;
    }
    
    public Page<Usuario> buscarTodoPorNombreDescendente(Pageable pageable){
    	return this.usuarioRepository.findAllByOrderByNombreDesc(pageable);
    }
    
    @Transactional
    public Usuario actualizarRole(Long id, String  name) {
    	Usuario usuarioConRol = usuarioRepository.getUserWithRoles(id)
    			.orElseThrow(()-> new UsernameNotFoundException("El usuario no fue encontrado"));
    	
    	Optional<Role> optionalRole =  roleRepository.findByName("ROLE_"+name);
    	optionalRole.ifPresent(usuarioConRol::addRole);
    	
    	return usuarioConRol;
    }
    
    public List<Usuario>  encontrarTecnicos(String name){
    	return usuarioRepository.findTecnicosDisponibles(name);
    }
}