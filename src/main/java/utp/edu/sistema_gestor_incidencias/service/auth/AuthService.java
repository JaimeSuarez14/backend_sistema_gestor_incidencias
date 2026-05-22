package utp.edu.sistema_gestor_incidencias.service.auth;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import utp.edu.sistema_gestor_incidencias.enums.Estado;
import utp.edu.sistema_gestor_incidencias.model.Role;
import utp.edu.sistema_gestor_incidencias.model.Usuario;
import utp.edu.sistema_gestor_incidencias.repository.RoleRepository;
import utp.edu.sistema_gestor_incidencias.repository.UsuarioRepository;

@Service
public class AuthService {
	private final UsuarioRepository userRepository;
	private  final  PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	
	public AuthService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	@Transactional
	public Usuario register(Usuario user) {
		String passwordHash = encryptPassword(user.getPasswordHash());
		user.setPasswordHash(passwordHash);
		
		Set<Role> roles = new HashSet<Role>();
		Optional<Role> optionalRole = roleRepository.findByName("ROLE_EMPLEADO");
		optionalRole.ifPresent(roles::add);
		
		user.setRoles(roles);
		user.setEstado(Estado.ACTIVO);
		
		return userRepository.save(user);
	}


	public String encryptPassword(String password) {
		return passwordEncoder.encode(password);
	}

}
