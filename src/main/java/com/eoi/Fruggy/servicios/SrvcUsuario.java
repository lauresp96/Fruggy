package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Cesta;
import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.Rol;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.repositorios.RepoRol;
import com.eoi.Fruggy.repositorios.RepoUsuario;
import com.eoi.Fruggy.repositorios.RepoValProducto;
import com.eoi.Fruggy.repositorios.RepoValSupermercado;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class SrvcUsuario extends AbstractSrvc<Usuario, Long, RepoUsuario> {

    private final RepoUsuario repoUsuario;
    private final RepoRol repoRol;
    private final PasswordEncoder passwordEncoder;
    private final RepoValProducto repoValProducto;
    private final RepoValSupermercado repoValSupermercado;


    // Se añaden estos métodos para agregar Bcrypt a contraseña cuando se cree nuevo usuario.
    @Autowired
    public SrvcUsuario(RepoUsuario repoUsuario, RepoRol repoRol, PasswordEncoder passwordEncoder, RepoValProducto repoValProducto, RepoValSupermercado repoValSupermercado) {
        super(repoUsuario);
        this.repoUsuario = repoUsuario;
        this.repoRol = repoRol;
        this.passwordEncoder = passwordEncoder;
        this.repoValProducto = repoValProducto;
        this.repoValSupermercado = repoValSupermercado;
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío en caso contrario.
     */
    public Optional<Usuario> findByEmail(String email) {
        return repoUsuario.findByEmail(email);
    }

    //Obtiene una lista de todos los usuarios.
    public List<Usuario> buscarEntidades() {
        return repoUsuario.findAll();
    }

    // Encuentra un usuario por su ID.
    public Optional<Usuario> encuentraPorId(long id) {
        return repoUsuario.findById((Long) id);
    }

    /*
     Guarda un usuario en la base de datos.
      Encripta la contraseña si no está vacía y establece los roles si están presentes.
     */
    @Override
    public Usuario guardar(Usuario usuario) throws Exception {
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        if (usuario.getRoles() != null) {
            usuario.setRoles(new HashSet<>(usuario.getRoles()));
        }
        return super.guardar(usuario);
    }

    //  Guarda un usuario con un conjunto de roles especificados.
    public Usuario guardar(Usuario usuario, Set<String> roles) throws Exception {
        Set<Rol> rolesSet = new HashSet<>();
        for (String rolNombre : roles) {
            Optional<Rol> adminRoleopt = repoRol.findByRolNombre(rolNombre);
            if (adminRoleopt.isPresent()) {
                rolesSet.add(adminRoleopt.get());
            }
        }
        usuario.setRoles(rolesSet);
        return guardar(usuario);
    }
    //Guarda o actualiza un usuario, comprobando que el correo electrónico no esté en uso por otro usuario.
    public void guardarEdicion(Usuario usuario) throws Exception {
        Optional<Usuario> existingUser = repoUsuario.findByEmail(usuario.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(usuario.getId())) {
            throw new Exception("El correo electrónico ya está en uso.");
        }
        repoUsuario.save(usuario);
    }

    //Obtiene los roles de un usuario por su ID para ordenar lista
    public Set<Rol> obtenerRolesPorUsuario(Long usuarioId) {
        Optional<Usuario> usuario = encuentraPorId(usuarioId);
        return usuario.map(Usuario::getRoles).orElse(new HashSet<>());
    }
    public void eliminarPorId(long id) {
        repoUsuario.deleteById((long) id);
    }

    // Paginación Usuarios y ordenamiento.
    public Page<Usuario> obtenerUsuariosPaginados(int page, int size, String sortField, String sortDirection, String email) {
        Sort sort;
        if ("roles".equals(sortField)) {
            sort = Sort.by(Sort.Direction.fromString(sortDirection), "roles");
        } else {
            sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        if (email != null && !email.isEmpty()) {
            return repoUsuario.findByEmailContainingIgnoreCase(email, pageable);
        } else {
            return repoUsuario.findAll(pageable);
        }
    }
    @Transactional
    public void eliminarPorId(Long id) {
        // Primero, actualizar las valoraciones para que ya no hagan referencia al usuario
        repoValProducto.updateUsuarioIdToNull(id);
        repoValSupermercado.updateUsuarioIdToNull(id);

        // Luego, eliminar el usuario
        repoUsuario.deleteById(id);
    }
}

