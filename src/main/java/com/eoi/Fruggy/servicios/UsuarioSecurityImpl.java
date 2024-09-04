package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Rol;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.repositorios.RepoUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementación de UserDetailsService para gestionar la autenticación de usuarios.
 * También provee funcionalidad para obtener contraseñas codificadas.
 */
@Service
public class UsuarioSecurityImpl implements IUsuarioSrvc, UserDetailsService {

    private final RepoUsuario repoUsuario;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UsuarioSecurityImpl.class);

    @Autowired
    public UsuarioSecurityImpl(RepoUsuario repoUsuario, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repoUsuario = repoUsuario;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //Obtiene la contraseña codificada para un usuario.
    @Override
    public String getEncodedPassword(Usuario usuario) {
        return bCryptPasswordEncoder.encode(usuario.getPassword());
    }

    //Carga un usuario por su email y lo convierte en un objeto UserDetails.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repoUsuario.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(rol.getRolNombre()));
        }

        // Devuelve el usuario que implementa UserDetails
        return usuario;
    }
}
