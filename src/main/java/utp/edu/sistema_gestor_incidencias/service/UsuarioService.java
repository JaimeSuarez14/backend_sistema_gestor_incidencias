package utp.edu.sistema_gestor_incidencias.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import utp.edu.sistema_gestor_incidencias.model.Usuario;

@Service
public class UsuarioService {

    private List<Usuario> usuarios = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public Usuario crearUsuario(Usuario usuario) {
        usuario.setId(idGenerator.getAndIncrement());
        this.usuarios.add(usuario);
        return usuario;
    }

    public Usuario modificarUsuario(Long id, Usuario datosNuevos) {
        Optional<Usuario> encontrado = obtenerUsuario(id);
        if (encontrado.isPresent()) {
            Usuario u = encontrado.get();
            u.setNombre(datosNuevos.getNombre());
            u.setCorreo(datosNuevos.getCorreo());
            u.setRol(datosNuevos.getRol());
            u.setArea(datosNuevos.getArea());
            u.setEstado(datosNuevos.getEstado());
            return u;
        }
        return null;
    }

    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public Optional<Usuario> obtenerUsuario(Long id) {
        return this.usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
}